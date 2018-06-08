package de.qaware.oss.cloud.service.payment.boundary;

import de.qaware.oss.cloud.service.payment.domain.PaymentEventLogStorage;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.opentracing.Traced;

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
    private PaymentEventLogStorage storage;

    @GET
    @Timed(unit = "milliseconds")
    @Traced(operationName = "GET /api/events")
    public Response events() {
        logger.info("GET all payment events.");
        return Response.ok(storage.all()).build();
    }

}
