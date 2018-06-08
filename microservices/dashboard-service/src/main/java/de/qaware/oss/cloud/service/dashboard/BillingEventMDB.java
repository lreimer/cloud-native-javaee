package de.qaware.oss.cloud.service.dashboard;

import io.opentracing.Tracer;
import io.opentracing.contrib.jms.common.TracingMessageListener;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.logging.Level;
import java.util.logging.Logger;

@MessageDriven(name = "BillingEventMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/BillingEvents"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto_acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "BILLING.EVENTS"),
        @ActivationConfigProperty(propertyName = "resourceAdapter", propertyValue = "activemq-rar")
})
public class BillingEventMDB implements MessageListener {

    @Inject
    private Logger logger;

    @Inject
    private DashboardEventHandler delegate;

    @Inject
    private Tracer tracer;

    @Override
    public void onMessage(Message message) {
        logger.log(Level.INFO, "Received inbound billing event message {0}.", message);

        new TracingMessageListener(m -> delegate.onMessage("BILLING.EVENTS", m), tracer).onMessage(message);
    }
}
