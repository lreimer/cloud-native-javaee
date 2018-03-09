package de.qaware.oss.cloud.service.process.boundary;

import com.codahale.metrics.annotation.Timed;
import de.qaware.oss.cloud.service.process.domain.ProcessEvent;
import de.qaware.oss.cloud.service.process.domain.ProcessStatusCache;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Path("process")
@Produces(MediaType.APPLICATION_JSON)
public class ProcessResource {

    @Inject
    private Logger logger;

    @Inject
    private Event<ProcessEvent> processEvent;

    @Inject
    private ProcessStatusCache statusCache;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed
    public Response process(JsonObject jsonObject) {
        logger.log(Level.INFO, "POST new process {0}", jsonObject);

        ProcessEvent event = ProcessEvent.created(jsonObject);
        processEvent.fire(event);

        return Response.accepted().build();
    }

    @GET
    @Path("/{processId}/status")
    @Timed
    public Response process(@PathParam("processId") String processId) {
        logger.log(Level.INFO, "GET process status for {0}", processId);

        Optional<ProcessEvent.EventType> eventType = statusCache.get(processId);
        ProcessEvent.EventType status = eventType.orElseThrow(NotFoundException::new);

        JsonObject payload = Json.createObjectBuilder()
                .add("processId", processId)
                .add("status", status.name())
                .build();

        return Response.ok(payload).build();
    }
}
