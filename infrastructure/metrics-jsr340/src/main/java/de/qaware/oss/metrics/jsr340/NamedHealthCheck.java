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
package de.qaware.oss.metrics.jsr340;

import com.codahale.metrics.health.HealthCheck;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A custom base class for named health checks.
 */
public abstract class NamedHealthCheck extends HealthCheck {

    private final String name;

    @Inject
    private Logger logger;

    /**
     * Create a health check without a name. {@link #getName()} must be overwritten explicitly
     */
    protected NamedHealthCheck() {
        name = "";
    }

    /**
     * Create a health check with given name
     *
     * @param name the name of the health check as exposed on endpoint
     */
    protected NamedHealthCheck(final String name) {
        this.name = name;
    }

    /**
     * Constructor for tests
     *
     * @param logger some logger
     */
    protected NamedHealthCheck(String name, Logger logger) {
        this(name);
        this.logger = logger;
    }

    /**
     * Returns the name the health check will be registered under.
     *
     * @return the health check name
     */
    public String getName() {
        return name;
    }

    @Override
    public Result execute() {
        try {
            Result result = check();
            if (!result.isHealthy()) {
                logger.log(Level.WARNING, "HealthCheck '{0}' is unhealthy: {1}",
                        new Object[]{getName(), result.getMessage()});
            }
            return result;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Exception in health check: {0}", new Object[]{getName(), e});
            return Result.unhealthy(e);
        }
    }
}
