package de.qaware.oss.cloud.service.process.boundary;

import com.codahale.metrics.annotation.Timed;
import de.qaware.oss.cloud.service.process.domain.ProcessEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed
    public Response process(JsonObject jsonObject) {
        logger.log(Level.INFO, "POST new process {0}", jsonObject);

        ProcessEvent event = ProcessEvent.created(jsonObject);
        processEvent.fire(event);

        return Response.accepted().build();
    }
}
