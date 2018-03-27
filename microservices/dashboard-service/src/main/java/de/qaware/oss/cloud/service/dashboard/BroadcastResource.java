package de.qaware.oss.cloud.service.dashboard;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;
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

    @PostConstruct
    public void initialize() {
        sseBroadcaster = sse.newBroadcaster();
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void fetch(@Context SseEventSink sseEventSink) {
        logger.info("Registering new SSE event sink with broadcaster.");
        sseBroadcaster.register(sseEventSink);
    }

    public void broadcast(@Observes DashboardEvent event) {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("eventType", event.getEventType())
                .add("payload", event.getPayload())
                .build();

        OutboundSseEvent broadcastEvent = sse.newEventBuilder()
                .name("event")
                .data(jsonObject)
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .build();

        logger.log(Level.INFO, "Broadcasting event {0}.", broadcastEvent);
        sseBroadcaster.broadcast(broadcastEvent);
    }
}
