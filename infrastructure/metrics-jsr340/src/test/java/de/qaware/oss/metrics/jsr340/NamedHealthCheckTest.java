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
import org.junit.Test;
import org.mockito.Mockito;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;

public class NamedHealthCheckTest {

    private Logger logger = Mockito.mock(Logger.class);

    private HealthCheck.Result result;
    private DummyNamedHealthCheck healthCheck = new DummyNamedHealthCheck();
    private ExceptionThrowingNamedHealthCheck healthCheckException = new ExceptionThrowingNamedHealthCheck();

    @Test
    public void checkHealthy() {
        result = HealthCheck.Result.healthy();
        assertThat(healthCheck.execute().isHealthy(), is(true));
    }

    @Test
    public void checkUnhealthy() {
        result = HealthCheck.Result.unhealthy("ill");
        assertThat(healthCheck.execute().isHealthy(), is(false));
        Mockito.verify(logger, Mockito.times(1)).log(eq(Level.WARNING), anyObject(), (Object[]) anyObject());
    }

    @Test
    public void checkUnhealthyWithException() {
        result = HealthCheck.Result.unhealthy("ill");
        assertThat(healthCheckException.execute().isHealthy(), is(false));
        Mockito.verify(logger, Mockito.times(1)).log(eq(Level.WARNING), anyObject(), (Object[]) anyObject());
    }

    @Test
    public void checkLegacyNamedHealthCheck() {
        LegacyNamedHealthCheck healthCheck = new LegacyNamedHealthCheck();
        assertThat(healthCheck.getName(), is("dummy"));
    }

    private class DummyNamedHealthCheck extends NamedHealthCheck {
        DummyNamedHealthCheck() {
            super("dummy", logger);
        }

        @Override
        protected Result check() {
            return result;
        }
    }

    private class ExceptionThrowingNamedHealthCheck extends NamedHealthCheck {
        ExceptionThrowingNamedHealthCheck() {
            super("dummy", logger);
        }

        @Override
        protected Result check() {
            throw new IllegalStateException("failed");
        }
    }

    private class LegacyNamedHealthCheck extends NamedHealthCheck {
        private LegacyNamedHealthCheck() {
            super();
        }

        @Override
        public String getName() {
            return "dummy";
        }

        @Override
        protected Result check() {
            return result;
        }
    }
}