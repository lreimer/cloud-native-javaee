package de.qaware.oss.cloud.service.process.boundary;

import com.codahale.metrics.annotation.Timed;
import de.qaware.oss.cloud.service.process.integration.ProcessServiceConfig;

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
    private ProcessServiceConfig config;

    @GET
    @Timed
    public Response config() {
        JsonObject result = Json.createObjectBuilder()
                .add("serviceName", config.serviceName())
                .add("processingSeconds", config.processingSeconds())
                .build();
        return Response.ok(result).build();
    }

}
