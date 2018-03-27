package de.qaware.oss.cloud.service.process.integration;

import org.apache.deltaspike.core.api.config.PropertyFileConfig;

/**
 * A custom property file configuration source. The file is optional,
 * the idea is to provide this file dynamically via the Docker container.
 * When run in OpenShift / Kubernetes we will mount a ConfigMap as file.
 */
public class CustomPropertyFileConfig implements PropertyFileConfig {

    @Override
    public String getPropertyFileName() {
        return "/process-service/config/application.properties";
    }

    @Override
    public boolean isOptional() {
        return true;
    }
}