package de.qaware.oss.cloud.service.dashboard;

import io.opentracing.contrib.cdi.Traced;
import org.eclipse.microprofile.metrics.annotation.Gauge;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Path("broadcast")
public class BroadcastResource {

    @Inject
    private Logger logger;

    @Context
    private Sse sse;
    private SseBroadcaster sseBroadcaster;

    private AtomicLong registeredEventSinks = new AtomicLong(0);

    @PostConstruct
    public void initialize() {
        sseBroadcaster = sse.newBroadcaster();

        sseBroadcaster.onClose((eventSink) -> {
            long count = registeredEventSinks.decrementAndGet();
            logger.log(Level.INFO, "Closing sink. Currently {0} events sinks listening.", count);
        });

        sseBroadcaster.onError((sseEventSink, throwable) -> {
            long count = registeredEventSinks.decrementAndGet();
            logger.log(Level.WARNING, "Error on event sink. Currently {0} events sinks listening.", new Object[]{count, throwable});
        });
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void fetch(@Context SseEventSink sseEventSink) {
        logger.info("Registering new SSE event sink with broadcaster.");
        sseBroadcaster.register(sseEventSink);

        long count = registeredEventSinks.incrementAndGet();
        logger.log(Level.INFO, "Currently {0} events sinks listening.", count);
    }

    @Gauge(unit = "none")
    public long registeredEventSinks() {
        return registeredEventSinks.get();
    }

    @Traced
    public void broadcast(@Observes DashboardEvent event) {
        OutboundSseEvent broadcastEvent = sse.newEventBuilder()
                .name("event")
                .data(event.toJson())
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .build();

        logger.log(Level.INFO, "Broadcasting event {0}.", broadcastEvent);
        sseBroadcaster.broadcast(broadcastEvent);
    }
}
