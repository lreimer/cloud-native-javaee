package de.qaware.oss.cloud.service.payment.domain;

import de.qaware.oss.cloud.service.payment.integration.PaymentServiceConfig;
import de.qaware.oss.cloud.service.payment.integration.ProcessEventQueue;

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
public class PaymentEventHandler {

    @Inject
    private Logger logger;

    @Inject
    private PaymentServiceConfig config;

    @Inject
    private ProcessEventQueue processEventQueue;

    public void observe(@Observes PaymentEvent paymentEvent) {
        PaymentEvent.EventType eventType = paymentEvent.getEventType();
        logger.log(Level.INFO, "{0} for {1}.", new Object[]{eventType, paymentEvent.getPayload()});

        switch (eventType) {
            case BillingInitiated:
                doSomeExpensivePayment();
                processEventQueue.publish(PaymentReceived.name(), paymentEvent.getPayload());
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
            e.printStackTrace();
        }
    }
}
