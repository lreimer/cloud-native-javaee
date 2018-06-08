package de.qaware.oss.cloud.service.payment.domain;

import de.qaware.oss.cloud.service.payment.integration.PaymentServiceConfig;
import de.qaware.oss.cloud.service.payment.integration.ProcessEventTopic;
import io.opentracing.contrib.cdi.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.qaware.oss.cloud.service.payment.domain.PaymentEvent.EventType.PaymentReceived;

/**
 * Handles any PaymentEvents.
 */
@ApplicationScoped
@Traced
public class PaymentEventHandler {

    @Inject
    private Logger logger;

    @Inject
    private PaymentServiceConfig config;

    @Inject
    private ProcessEventTopic processEventTopic;

    @Inject
    private PaymentEventLogStorage storage;

    public void observe(@Observes PaymentEvent paymentEvent) {
        PaymentEvent.EventType eventType = paymentEvent.getEventType();
        logger.log(Level.INFO, "{0} for {1}.", new Object[]{eventType, paymentEvent.getPayload()});

        switch (eventType) {
            case BillingInitiated:
                doSomeExpensivePayment();
                PaymentEvent next = paymentEvent.transitionTo(PaymentReceived);
                processEventTopic.publish(next.getEventType().name(), next.getPayload());
                storage.store(next);
                break;
            default:
                logger.log(Level.WARNING, "Unknown EventType {0}.", eventType);
                break;
        }
    }

    private void doSomeExpensivePayment() {
        try {
            TimeUnit.SECONDS.sleep(config.processingSeconds());
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "Could not do some expensive payment.", e);
        }
    }
}
