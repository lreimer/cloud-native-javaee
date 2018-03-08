/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 QAware GmbH, Munich, Germany
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.qaware.oss.hystrix.jsr236;

import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.properties.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A Hystrix concurrency implementation that uses JSR 236 managed thread factories
 * to construct the thread pool.
 * <p>
 * This implementation uses the {@link HystrixThreadPoolKey} to perform a JNDI lookup
 * to obtain a JEE concurrency {@link ManagedThreadFactory} instance.
 * <pre>
 *      "java:concurrent/" + threadPoolKey.name() + "ThreadFactory"
 * </pre>
 * <p>
 * In case you are using Glassfish v4.1 or Payara Fish, you can create the managed thread
 * factory by using the asadmin command, e.g.
 * <pre>
 *      asadmin --user admin --passwordfile=/opt/pwdfile create-managed-thread-factory concurrent/BackendThreadFactory
 * </pre>
 * <p>
 * In you code you can then construct Hystrix command implementations and specify a matching
 * <pre>
 *     public class GetStuffHystrixCommand extends HystrixCommand<Stuff> {
 *          public GetUserHystrixCommand() {
 *              super(
 *                  Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Backend"))
 *                        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("Backend")))
 *          }
 *     }
 * </pre>
 */
public class HystrixConcurrencyStrategyJsr236 extends HystrixConcurrencyStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(HystrixConcurrencyStrategyJsr236.class);

    private final Context context;

    /**
     * Initialize the concurrency strategy and its initial JNDI context.
     */
    public HystrixConcurrencyStrategyJsr236() {
        context = initialContext();
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                            HystrixProperty<Integer> corePoolSize,
                                            HystrixProperty<Integer> maximumPoolSize,
                                            HystrixProperty<Integer> keepAliveTime,
                                            TimeUnit unit, BlockingQueue<Runnable> workQueue) {

        ThreadFactory threadFactory = lookupManagedThreadFactory(threadPoolKey);
        if (threadFactory != null) {
            return new ThreadPoolExecutor(corePoolSize.get(), maximumPoolSize.get(), keepAliveTime.get(),
                    unit, workQueue, threadFactory);
        } else {
            LOGGER.warn("Fallback to Hystrix default thread pool executor.");
            return super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }
    }

    private InitialContext initialContext() {
        try {
            return new InitialContext();
        } catch (NamingException e) {
            LOGGER.warn("Unable to obtain initial JNDI context.", e);
            return null;
        }
    }

    /**
     * Thy to get a {@link ManagedThreadFactory} instance using the name of the given {@link HystrixThreadPoolKey}.
     * The method will per form a JNDI lookup using the "java:concurrent/" + threadPoolKey.name() + "ThreadFactory".
     *
     * @param threadPoolKey the thread pool key
     * @return NULL of not found in JNDI, otherwise the managed thread factory instance
     */
    private ManagedThreadFactory lookupManagedThreadFactory(HystrixThreadPoolKey threadPoolKey) {
        if (context == null) {
            return null;
        }

        String name = "java:concurrent/" + threadPoolKey.name() + "ThreadFactory";

        Object thing = null;
        try {
            thing = context.lookup(name);
        } catch (NamingException e) {
            LOGGER.warn("Unable to lookup managed thread factory.", e);
        }

        if (thing instanceof ManagedThreadFactory) {
            return (ManagedThreadFactory) thing;
        } else if (thing == null) {
            LOGGER.warn("Unable to find a JSR 236 managed thread factory using {} in JDNI.", name);
            return null;
        } else {
            LOGGER.warn("Found thing is not a JSR 236 managed thread factory, it is a {} instead.", thing);
            return null;
        }
    }
}
