/*
 *    ___  _ _____                   ___                           __
 *   / _ )(_) / (_)__  ___ _  _//   / _ \___ ___ ____ _  ___ ___  / /_
 *  / _  / / / / / _ \/ _ `/ (_-<  / ___/ _ `/ // /  ' \/ -_) _ \/ __/
 * /____/_/_/_/_/_//_/\_, / / __/ /_/   \_,_/\_, /_/_/_/\__/_//_/\__/
 *                   /___/  //              /___/
 *                                                   (c) 2017 BMW AG
 */
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

