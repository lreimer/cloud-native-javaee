package de.qaware.oss.tracing.jsr346;

import io.jaegertracing.Configuration;
import io.opentracing.Tracer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class TracerProducer {

    @Produces
    @Alternative
    @ApplicationScoped
    public Tracer getJaegerTracerFromEnv() {
        return Configuration.fromEnv().getTracer();
    }
}
