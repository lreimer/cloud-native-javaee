package de.qaware.oss.cloud.service.billing.boundary;

import de.qaware.oss.cloud.service.billing.integration.BillingServiceConfig;
import io.opentracing.Span;
import io.opentracing.util.GlobalTracer;
import org.apache.deltaspike.core.api.projectstage.ProjectStage;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("config")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigResource {

    @Inject
    private ProjectStage projectStage;

    @Inject
    private BillingServiceConfig config;

    @GET
    @Timed(unit = "milliseconds")
    public Response config() {

        Span childSpan = GlobalTracer.get().buildSpan("GET config").start();

        JsonObject result = Json.createObjectBuilder()
                .add("projectStage", projectStage.toString())
                .add("serviceName", config.serviceName())
                .add("processingSeconds", config.processingSeconds())
                .build();
        childSpan.finish();
        return Response.ok(result).build();
    }

}
