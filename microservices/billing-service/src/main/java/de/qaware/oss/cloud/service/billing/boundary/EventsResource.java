package de.qaware.oss.cloud.service.billing.boundary;

import de.qaware.oss.cloud.service.billing.domain.BillingEventLog;
import de.qaware.oss.cloud.service.billing.domain.BillingEventLogStorage;
import io.opentracing.Span;
import io.opentracing.util.GlobalTracer;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
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
    @Timed(unit = "milliseconds")
    public Response events() {
        Span childSpan = GlobalTracer.get().buildSpan("GET all billing events.").start();

        logger.info("GET all billing events.");
        Collection<BillingEventLog> events = storage.all();
        childSpan.finish();
        return Response.ok(events).build();
    }

}
