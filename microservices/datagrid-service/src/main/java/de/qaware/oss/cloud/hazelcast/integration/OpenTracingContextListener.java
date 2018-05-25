package de.qaware.oss.cloud.hazelcast.integration;

import io.jaegertracing.Configuration;
import io.opentracing.Tracer;
import io.opentracing.contrib.jaxrs2.server.SpanFinishingFilter;
import io.opentracing.util.GlobalTracer;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import java.util.EnumSet;

@WebListener
public class OpenTracingContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Tracer tracer = Configuration.fromEnv().getTracer();
        GlobalTracer.register(tracer);

        ServletContext servletContext = sce.getServletContext();
        FilterRegistration.Dynamic filterRegistration = servletContext
                .addFilter("tracingFilter", new SpanFinishingFilter(tracer));
        filterRegistration.setAsyncSupported(true);
        filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
