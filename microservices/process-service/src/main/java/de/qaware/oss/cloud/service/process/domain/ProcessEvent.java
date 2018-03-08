package de.qaware.oss.cloud.service.process.domain;

import lombok.Data;

import javax.json.JsonObject;

@Data
public class ProcessEvent {

    public enum EventType {
        ProcessCreated, BillingInitiated, PaymentReceived;
    }

    private final String processId;
    private final EventType eventType;
    private final JsonObject payload;

    private ProcessEvent(EventType eventType, JsonObject payload) {
        this.processId = payload.getString("processId");
        this.eventType = eventType;
        this.payload = payload;
    }

    public static ProcessEvent created(JsonObject payload) {
        return new ProcessEvent(EventType.ProcessCreated, payload);
    }

    public static ProcessEvent from(String eventTypeValue, JsonObject payload) {
        EventType eventType = EventType.valueOf(eventTypeValue);
        return new ProcessEvent(eventType, payload);
    }
}
