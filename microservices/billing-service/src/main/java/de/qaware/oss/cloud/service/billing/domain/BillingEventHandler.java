package de.qaware.oss.cloud.service.billing.domain;

import de.qaware.oss.cloud.service.billing.integration.BillingServiceConfig;
import de.qaware.oss.cloud.service.billing.integration.ProcessEventTopic;
import io.opentracing.contrib.cdi.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.qaware.oss.cloud.service.billing.domain.BillingEvent.EventType.BillingInitiated;

/**
 * Handle any BillingEvents.
 */
@ApplicationScoped
@Traced
public class BillingEventHandler {

    @Inject
    private Logger logger;

    @Inject
    private BillingServiceConfig config;

    @Inject
    private ProcessEventTopic processEventTopic;

    @Inject
    private BillingEventLogStorage storage;

    public void observe(@Observes BillingEvent billingEvent) {
        BillingEvent.EventType eventType = billingEvent.getEventType();
        logger.log(Level.INFO, "{0} for {1}.", new Object[]{eventType, billingEvent.getPayload()});

        switch (eventType) {
            case ProcessCreated:
                doSomeSeriousBilling();
                BillingEvent next = billingEvent.transitionTo(BillingInitiated);
                processEventTopic.publish(next.getEventType().name(), next.getPayload());
                storage.store(next);
                break;
            default:
                logger.log(Level.WARNING, "Unknown EventType {0}.", eventType);
                break;
        }
    }

    private void doSomeSeriousBilling() {
        try {
            TimeUnit.SECONDS.sleep(config.processingSeconds());
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "Could not do some serious billing.", e);
        }
    }
}
