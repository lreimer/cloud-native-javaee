package de.qaware.oss.cloud.service.process.integration;

import io.opentracing.Tracer;
import io.opentracing.contrib.jms.TracingMessageProducer;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.*;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class PaymentEventTopic {

    @Inject
    private Logger logger;

    @Resource(lookup = "jms/activeMqConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/PaymentEvents")
    private Topic destination;

    @Inject
    private Tracer tracer;

    public void publish(String eventType, JsonObject paymentEvent) {
        try (Connection connection = connectionFactory.createConnection()) {
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = new TracingMessageProducer(session.createProducer(destination), tracer);
            // producer.setDeliveryDelay(1000);    // 1 second
            producer.setTimeToLive(1000 * 300); // 5 minutes

            StringWriter payload = new StringWriter();
            JsonWriter jsonWriter = Json.createWriter(payload);
            jsonWriter.writeObject(paymentEvent);

            TextMessage textMessage = session.createTextMessage(payload.toString());
            textMessage.setJMSType(eventType);
            textMessage.setStringProperty("contentType", "application/vnd.payment.v1+json");

            producer.send(textMessage);
            logger.log(Level.INFO, "Sent {0} to BillingEvents destination.", textMessage);
        } catch (JMSException e) {
            logger.log(Level.WARNING, "Could not send JMS message.", e);
        }
    }
}
