/*
 *    ___  _ _____                   ___                           __
 *   / _ )(_) / (_)__  ___ _  _//   / _ \___ ___ ____ _  ___ ___  / /_
 *  / _  / / / / / _ \/ _ `/ (_-<  / ___/ _ `/ // /  ' \/ -_) _ \/ __/
 * /____/_/_/_/_/_//_/\_, / / __/ /_/   \_,_/\_, /_/_/_/\__/_//_/\__/
 *                   /___/  //              /___/
 *                                                   (c) 2017 BMW AG
 */
package de.qaware.oss.cloud.service.billing.integration;

import org.apache.deltaspike.core.api.config.PropertyFileConfig;

/**
 * A custom property file configuration source. The file is optional,
 * the idea is to provide this file dynamically via the Docker container.
 * When run in OpenShift / Kubernetes we will mount a ConfigMap as file.
 */
public class CustomPropertyFileConfig implements PropertyFileConfig {

    @Override
    public String getPropertyFileName() {
        return "/billing-service/config/application.properties";
    }

    @Override
    public boolean isOptional() {
        return true;
    }
}