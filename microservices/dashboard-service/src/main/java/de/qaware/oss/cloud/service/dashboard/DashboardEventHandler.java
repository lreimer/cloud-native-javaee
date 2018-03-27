package de.qaware.oss.cloud.service.dashboard;

import javax.enterprise.context.ApplicationScoped;
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

@ApplicationScoped
public class DashboardEventHandler implements MessageListener {

    @Inject
    private Logger logger;

    @Inject
    private Event<DashboardEvent> events;

    @Override
    public void onMessage(Message message) {
        String eventType = getEventType(message);
        String body = getBody(message);

        if ((eventType != null) && (body != null)) {
            JsonReader reader = Json.createReader(new StringReader(body));
            events.fire(new DashboardEvent(eventType, reader.readObject()));
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
