/*
 *    ___  _ _____                   ___                           __
 *   / _ )(_) / (_)__  ___ _  _//   / _ \___ ___ ____ _  ___ ___  / /_
 *  / _  / / / / / _ \/ _ `/ (_-<  / ___/ _ `/ // /  ' \/ -_) _ \/ __/
 * /____/_/_/_/_/_//_/\_, / / __/ /_/   \_,_/\_, /_/_/_/\__/_//_/\__/
 *                   /___/  //              /___/
 *                                                   (c) 2017 BMW AG
 */
package de.qaware.oss.cloud.service.payment.integration;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.apache.deltaspike.core.api.config.Configuration;

/**
 * The configuration interface for the Payment service.
 */
@Configuration(cacheFor = 30)
public interface PaymentServiceConfig {

    @ConfigProperty(name = "service.name", defaultValue = "Payment Service (Default)")
    String serviceName();

    @ConfigProperty(name = "processing.seconds", defaultValue = "2")
    int processingSeconds();

}

