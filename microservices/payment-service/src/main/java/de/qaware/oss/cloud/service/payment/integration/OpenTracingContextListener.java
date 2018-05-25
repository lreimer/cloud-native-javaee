package de.qaware.oss.cloud.service.payment.integration;

import io.jaegertracing.Configuration;
import io.opentracing.Tracer;
import io.opentracing.contrib.jaxrs2.server.SpanFinishingFilter;
import io.opentracing.util.GlobalTracer;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import java.util.EnumSet;
import java.util.logging.Logger;

@WebListener
public class OpenTracingContextListener implements ServletContextListener {

    @Inject
    private Logger logger;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Initializing OpenTracing");

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
