package de.qaware.oss.cloud.service.payment.domain;

import lombok.Data;

import javax.json.JsonObject;

@Data
public class PaymentEvent {

    public enum EventType {
        BillingInitiated, PaymentReceived;
    }

    private final String processId;
    private final EventType eventType;
    private final JsonObject payload;

    private PaymentEvent(EventType eventType, JsonObject payload) {
        this.processId = payload.getString("processId");
        this.eventType = eventType;
        this.payload = payload;
    }

    public static PaymentEvent from(String eventTypeValue, JsonObject payload) {
        EventType eventType = EventType.valueOf(eventTypeValue);
        return new PaymentEvent(eventType, payload);
    }

    public PaymentEvent transitionTo(EventType eventType) {
        return new PaymentEvent(eventType, this.getPayload());
    }
}
