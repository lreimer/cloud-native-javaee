package de.qaware.oss.tracing.jsr346;

import io.jaegertracing.Configuration;
import io.opentracing.Tracer;
import lombok.extern.java.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;

@ApplicationScoped
@Log
public class TracerProducer {

    @Produces
    @Alternative
    @ApplicationScoped
    public Tracer getJaegerTracerFromEnv() {
        log.info("Creating Jaeger tracer from ENV.");
        return Configuration.fromEnv().getTracer();
    }
}
