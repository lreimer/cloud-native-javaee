package de.qaware.oss.cloud.service.dashboard;

import io.opentracing.contrib.cdi.Traced;
import io.opentracing.contrib.jaxrs2.client.ClientTracingFeature;
import org.eclipse.microprofile.faulttolerance.Timeout;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class ProcessServiceClient {

    private Client client;
    private WebTarget processService;

    @PostConstruct
    void initialize() {
        client = ClientBuilder.newBuilder()
                .register(ClientTracingFeature.class)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        processService = client.target("http://process-service:8080").path("/api/process");
    }

    @PreDestroy
    void destroy() {
        client.close();
    }

    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    @Traced
    public void send(String processId, String name, Long amount) {
        JsonObject payload = Json.createObjectBuilder()
                .add("processId", processId)
                .add("name", name)
                .add("amount", amount)
                .build();

        Response response = processService.request().post(Entity.json(payload));
        Response.StatusType statusInfo = response.getStatusInfo();
        if (!Response.Status.Family.SUCCESSFUL.equals(statusInfo.getFamily())) {
            throw new BadRequestException(statusInfo.getReasonPhrase());
        }
    }
}
