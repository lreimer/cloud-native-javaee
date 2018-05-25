package de.qaware.oss.cloud.service.dashboard;

import io.opentracing.contrib.jaxrs2.server.ServerTracingDynamicFeature;
import io.opentracing.util.GlobalTracer;

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
public class DashboardServiceAPI extends Application {

    @Override
    public Set<Object> getSingletons() {
        DynamicFeature tracing = new ServerTracingDynamicFeature.Builder(GlobalTracer.get())
                .build();
        return Collections.singleton(tracing);
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        classes.add(BroadcastResource.class);
        classes.add(GuiResource.class);
        return classes;
    }
}
