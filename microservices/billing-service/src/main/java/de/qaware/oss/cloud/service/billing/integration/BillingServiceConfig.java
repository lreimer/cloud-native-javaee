package de.qaware.oss.cloud.service.billing.integration;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.apache.deltaspike.core.api.config.Configuration;

/**
 * The configuration interface for the Billing service.
 */
@Configuration(cacheFor = 60)
public interface BillingServiceConfig {

    @ConfigProperty(name = "service.name", defaultValue = "Billing Service (Default)")
    String serviceName();

    @ConfigProperty(name = "processing.seconds", defaultValue = "2")
    int processingSeconds();

}

