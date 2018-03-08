package de.qaware.oss.cloud.service.billing.boundary;

import com.codahale.metrics.annotation.Timed;
import de.qaware.oss.cloud.service.billing.domain.BillingEventLogStorage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@ApplicationScoped
@Path("events")
@Produces(MediaType.APPLICATION_JSON)
public class EventsResource {

    @Inject
    private Logger logger;

    @Inject
    private BillingEventLogStorage storage;

    @GET
    @Timed
    public Response events() {
        logger.info("GET all billing events.");
        return Response.ok(storage.all()).build();
    }

}
