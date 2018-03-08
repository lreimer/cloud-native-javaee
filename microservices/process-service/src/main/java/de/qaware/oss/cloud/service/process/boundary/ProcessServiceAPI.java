package de.qaware.oss.cloud.service.process.boundary;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jersey2.InstrumentedResourceMethodApplicationListener;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * The REST API application.
 */
@ApplicationPath("/api/")
public class ProcessServiceAPI extends Application {

    @Inject
    private MetricRegistry registry;

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        classes.add(JacksonFeature.class);

        classes.add(ProcessResource.class);
        classes.add(EventsResource.class);
        classes.add(ConfigResource.class);

        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> singletons = new HashSet<>();
        singletons.add(new InstrumentedResourceMethodApplicationListener(registry));
        return singletons;
    }
}
