package de.qaware.oss.cloud.service.dashboard;

import lombok.Data;

import javax.json.Json;
import javax.json.JsonObject;

@Data
public class DashboardEvent {

    private final String destination;
    private final String eventType;
    private final JsonObject payload;

    /**
     * Return the JSON representation of this event.
     *
     * @return the JSON representation
     */
    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("destination", getDestination())
                .add("eventType", getEventType())
                .add("payload", getPayload())
                .build();
    }
}
