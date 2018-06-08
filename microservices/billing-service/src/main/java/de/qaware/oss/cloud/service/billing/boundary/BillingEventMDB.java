package de.qaware.oss.cloud.service.billing.boundary;

import de.qaware.oss.cloud.service.billing.domain.BillingEvent;
import io.opentracing.Tracer;
import io.opentracing.contrib.jms.common.TracingMessageListener;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.json.Json;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

@MessageDriven(name = "BillingEventMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/BillingEvents"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "BILLING.EVENTS"),
        @ActivationConfigProperty(propertyName = "resourceAdapter", propertyValue = "activemq-rar"),
        @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
        @ActivationConfigProperty(propertyName = "clientId", propertyValue = "billing-service"),
        @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "BillingEventMDB"),
        @ActivationConfigProperty(propertyName = "messageSelector",
                propertyValue = "contentType = 'application/vnd.billing.v1+json'")
})
public class BillingEventMDB implements MessageListener {

    @Inject
    private Logger logger;

    @Inject
    private Event<BillingEvent> billingEvent;

    @Inject
    private Tracer tracer;

    @Override
    public void onMessage(Message message) {
        logger.log(Level.INFO, "Received inbound billing event message {0}.", message);

        new TracingMessageListener(this::onInstrumentedMessage, tracer).onMessage(message);
    }

    private String getBody(Message message) {
        String body = null;
        try {
            if (message instanceof TextMessage) {
                body = ((TextMessage) message).getText();
            }
        } catch (JMSException e) {
            logger.log(Level.WARNING, "Could not get message body.", e);
        }
        return body;
    }

    private String getEventType(Message message) {
        String eventType = null;
        try {
            eventType = message.getJMSType();
        } catch (JMSException e) {
            logger.log(Level.WARNING, "Could not get JMS type.", e);
        }
        return eventType;
    }

    private void onInstrumentedMessage(Message msg) {
        String eventType = getEventType(msg);
        String body = getBody(msg);

        if ((eventType != null) && (body != null)) {
            try (JsonReader reader = Json.createReader(new StringReader(body))) {
                billingEvent.fire(BillingEvent.from(eventType, reader.readObject()));
            }
        }
    }
}
