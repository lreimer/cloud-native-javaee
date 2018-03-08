package de.qaware.oss.cloud.service.billing.domain;

import lombok.Data;

import javax.json.JsonObject;

@Data
public class BillingEvent {

    public enum EventType {
        ProcessCreated, BillingInitiated
    }

    private final String processId;
    private final EventType eventType;
    private final JsonObject payload;

    private BillingEvent(EventType eventType, JsonObject payload) {
        this.processId = payload.getString("processId");
        this.eventType = eventType;
        this.payload = payload;
    }

    public static BillingEvent from(String eventTypeValue, JsonObject payload) {
        EventType eventType = EventType.valueOf(eventTypeValue);
        return new BillingEvent(eventType, payload);
    }

    public BillingEvent transitionTo(EventType eventType) {
        return new BillingEvent(eventType, this.getPayload());
    }
}
