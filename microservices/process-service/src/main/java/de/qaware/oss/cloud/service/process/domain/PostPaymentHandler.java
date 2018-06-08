package de.qaware.oss.cloud.service.process.domain;

import de.qaware.oss.cloud.service.process.integration.BillingEventTopic;
import de.qaware.oss.cloud.service.process.integration.PaymentEventTopic;
import de.qaware.oss.cloud.service.process.integration.ProcessServiceConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handle the events for a post-payment process.
 */
@ApplicationScoped
public class PostPaymentHandler {

    @Inject
    private Logger logger;

    @Inject
    private ProcessServiceConfig config;

    @Inject
    private BillingEventTopic billingEventTopic;

    @Inject
    private PaymentEventTopic paymentEventTopic;

    public void observe(@Observes ProcessEvent processEvent) {
        ProcessEvent.EventType eventType = processEvent.getEventType();
        logger.log(Level.INFO, "{0} for {1}.", new Object[]{eventType, processEvent.getPayload()});

        switch (eventType) {
            case ProcessCreated:
                doSomeHeavyProcessing();
                billingEventTopic.publish(eventType.name(), processEvent.getPayload());
                break;
            case BillingInitiated:
                doSomeHeavyProcessing();
                paymentEventTopic.publish(eventType.name(), processEvent.getPayload());
                break;
            case PaymentReceived:
                break;
            default:
                logger.log(Level.WARNING, "Unknown EventType {0}.", eventType);
                break;
        }
    }

    private void doSomeHeavyProcessing() {
        try {
            TimeUnit.SECONDS.sleep(config.processingSeconds());
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "Could not do some heavy processing.", e);
        }
    }
}
