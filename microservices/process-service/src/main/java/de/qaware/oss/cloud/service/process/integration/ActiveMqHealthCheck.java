/*
 *    ___  _ _____                   ___                           __
 *   / _ )(_) / (_)__  ___ _  _//   / _ \___ ___ ____ _  ___ ___  / /_
 *  / _  / / / / / _ \/ _ `/ (_-<  / ___/ _ `/ // /  ' \/ -_) _ \/ __/
 * /____/_/_/_/_/_//_/\_, / / __/ /_/   \_,_/\_, /_/_/_/\__/_//_/\__/
 *                   /___/  //              /___/
 *                                                   (c) 2017 BMW AG
 */
package de.qaware.oss.cloud.service.process.integration;

import com.codahale.metrics.health.HealthCheck;
import de.qaware.oss.metrics.jsr340.JmsConnectionHealthChecker;
import de.qaware.oss.metrics.jsr340.NamedHealthCheck;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.jms.ConnectionFactory;

/**
 * Health check for the message queue.
 * <p/>
 * Try to create connections to the message queue to make sure it is available
 */
@ApplicationScoped
public class ActiveMqHealthCheck extends NamedHealthCheck {

    private final JmsConnectionHealthChecker healthChecker;

    @Resource(lookup = "jms/activeMqConnectionFactory")
    private ConnectionFactory connectionFactory;

    protected ActiveMqHealthCheck() {
        super("ActiveMq");
        healthChecker = new JmsConnectionHealthChecker();
    }

    @Override
    protected HealthCheck.Result check() {
        return healthChecker.check(connectionFactory);
    }
}
