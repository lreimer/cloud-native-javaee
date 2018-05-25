package de.qaware.oss.cloud.hazelcast;

import io.opentracing.contrib.jaxrs2.server.ServerTracingDynamicFeature;
import io.opentracing.util.GlobalTracer;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.Set;

/**
 * The REST API application.
 */
@ApplicationPath("/api/")
public class RestAPI extends Application {

    @Override
    public Set<Object> getSingletons() {
        DynamicFeature tracing = new ServerTracingDynamicFeature.Builder(GlobalTracer.get())
                .build();
        return Collections.singleton(tracing);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return Collections.singleton(CacheResource.class);
    }
}
