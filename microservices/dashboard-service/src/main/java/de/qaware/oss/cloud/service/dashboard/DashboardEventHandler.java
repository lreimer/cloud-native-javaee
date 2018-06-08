package de.qaware.oss.cloud.service.dashboard;

import io.opentracing.contrib.cdi.Traced;
import org.eclipse.microprofile.metrics.annotation.Counted;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.json.Json;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class DashboardEventHandler {

    @Inject
    private Logger logger;

    @Inject
    private Event<DashboardEvent> events;

    @Counted(monotonic = true)
    @Traced
    public void onMessage(String destination, Message message) {
        String eventType = getEventType(message);
        String body = getBody(message);

        if ((eventType != null) && (body != null)) {
            try (JsonReader reader = Json.createReader(new StringReader(body))) {
                events.fire(new DashboardEvent(destination, eventType, reader.readObject()));
            }
        }
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

}
