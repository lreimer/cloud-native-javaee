package de.qaware.oss.cloud.service.process.boundary;

import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * The REST API application.
 */
@ApplicationPath("/api/")
public class ProcessServiceAPI extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        classes.add(JacksonFeature.class);

        classes.add(ProcessResource.class);
        classes.add(EventsResource.class);
        classes.add(ConfigResource.class);

        return classes;
    }
}
