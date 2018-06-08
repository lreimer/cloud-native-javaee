package de.qaware.oss.cloud.service.process.boundary;

import de.qaware.oss.cloud.service.process.domain.ProcessEvent;
import de.qaware.oss.cloud.service.process.domain.ProcessStatusCache;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.opentracing.Traced;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
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

    @Resource
    private ManagedExecutorService executorService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(unit = "milliseconds")
    public void process(@Suspended AsyncResponse response, @NotNull JsonObject jsonObject) {
        logger.log(Level.INFO, "POST new process {0}", jsonObject);

        response.setTimeout(10, TimeUnit.SECONDS);
        response.setTimeoutHandler((r) -> r.resume(Response.accepted().build()));

        executorService.execute(() -> {
            ProcessEvent event = ProcessEvent.created(jsonObject);
            processEvent.fire(event);

            response.resume(Response.accepted(event).build());
        });
    }

    @GET
    @Path("/{processId}/status")
    @Timed(unit = "milliseconds")
    @Traced(operationName = "GET /api/process")
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
