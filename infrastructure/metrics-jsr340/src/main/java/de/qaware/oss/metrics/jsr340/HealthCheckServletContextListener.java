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

import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.HealthCheckServlet;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;

/**
 * The custom health check servlet context listener implementation. This is required for the
 * correct integration of the Metrics servlets.
 * <p>
 * If you are in a Servlet 3.0 environment you do not need to do anything. For Servlet 2.4 you
 * have to add the follow snippet to your web.xml file
 * <pre>
 *     &lt;listener&gt;
 *         &lt;listener-class&gt;com.bmw.iap.bdr.commons.metrics.HealthCheckServletContextListener&lt;/listener-class&gt;
 *     &lt;/listener&gt;
 * </pre>
 */
@WebListener
public class HealthCheckServletContextListener extends HealthCheckServlet.ContextListener {

    @Inject
    private HealthCheckRegistry registry;

    @Override
    protected HealthCheckRegistry getHealthCheckRegistry() {
        return registry;
    }
}
