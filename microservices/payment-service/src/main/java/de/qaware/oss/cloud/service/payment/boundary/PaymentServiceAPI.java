package de.qaware.oss.cloud.service.payment.boundary;

import io.opentracing.Tracer;
import io.opentracing.contrib.jaxrs2.server.ServerTracingDynamicFeature;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The REST API application.
 */
@ApplicationPath("/api/")
public class PaymentServiceAPI extends Application {

    @Inject
    private Tracer tracer;

    @Override
    public Set<Object> getSingletons() {
        DynamicFeature tracing = new ServerTracingDynamicFeature.Builder(tracer).build();
        return Collections.singleton(tracing);
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        classes.add(JacksonFeature.class);

        classes.add(EventsResource.class);
        classes.add(ConfigResource.class);

        return classes;
    }
}
