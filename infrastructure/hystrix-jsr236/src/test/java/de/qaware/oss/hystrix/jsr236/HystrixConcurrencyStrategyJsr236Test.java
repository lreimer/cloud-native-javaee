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
import com.netflix.hystrix.strategy.properties.HystrixProperty;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.naming.Context;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * A basic unit test to check the proper JNDI lookup behaviour of our JSR 236 strategy implementation.
 *
 * @author lreimer
 */
public class HystrixConcurrencyStrategyJsr236Test {

    @Rule
    public MockInitialContextRule rule = new MockInitialContextRule(mock(Context.class));

    private HystrixConcurrencyStrategyJsr236 concurrencyStrategy;
    private ManagedThreadFactory mtf;

    @Before
    public void setUp() throws Exception {
        concurrencyStrategy = new HystrixConcurrencyStrategyJsr236();

        mtf = mock(ManagedThreadFactory.class);
        when(rule.getContext().lookup("java:concurrent/testThreadFactory")).thenReturn(mtf);
    }

    @Test
    public void getManagedThreadPool() throws Exception {
        ThreadPoolExecutor threadPool = concurrencyStrategy.getThreadPool(HystrixThreadPoolKey.Factory.asKey("test"),
                HystrixProperty.Factory.asProperty(10),
                HystrixProperty.Factory.asProperty(10),
                HystrixProperty.Factory.asProperty(60),
                TimeUnit.SECONDS, new SynchronousQueue<>());

        assertThat(threadPool, is(notNullValue()));
        assertThat(threadPool.getThreadFactory(), is(mtf));
    }

    @Test
    public void getUnknownThreadPool() throws Exception {
        ThreadPoolExecutor threadPool = concurrencyStrategy.getThreadPool(HystrixThreadPoolKey.Factory.asKey("unknown"),
                HystrixProperty.Factory.asProperty(10),
                HystrixProperty.Factory.asProperty(10),
                HystrixProperty.Factory.asProperty(60),
                TimeUnit.SECONDS, new SynchronousQueue<>());

        assertThat(threadPool, is(notNullValue()));
        assertThat(threadPool.getThreadFactory(), not(mtf));
    }

    @Test
    public void getNoManagedThreadPool() throws Exception {
        reset(rule.getContext());
        when(rule.getContext().lookup("java:concurrent/test")).thenReturn("NoManagedThreadPool");

        ThreadPoolExecutor threadPool = concurrencyStrategy.getThreadPool(HystrixThreadPoolKey.Factory.asKey("test"),
                HystrixProperty.Factory.asProperty(10),
                HystrixProperty.Factory.asProperty(10),
                HystrixProperty.Factory.asProperty(60),
                TimeUnit.SECONDS, new SynchronousQueue<>());

        assertThat(threadPool, is(notNullValue()));
        assertThat(threadPool.getThreadFactory(), not(mtf));
    }
}