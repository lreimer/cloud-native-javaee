package de.qaware.oss.cloud.service.process.integration;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.apache.deltaspike.core.api.config.Configuration;

/**
 * The configuration interface for the Process service.
 */
@Configuration(cacheFor = 60)
public interface ProcessServiceConfig {

    @ConfigProperty(name = "service.name", defaultValue = "Process Service (Default)")
    String serviceName();

    @ConfigProperty(name = "processing.seconds", defaultValue = "1")
    int processingSeconds();

}

