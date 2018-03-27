package de.qaware.oss.cloud.service.process.integration;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

/**
 * Health check for the message queue.
 * <p/>
 * Try to create connections to the message queue to make sure it is available
 */
@ApplicationScoped
@Health
public class ActiveMqHealthCheck implements HealthCheck {

    @Resource(lookup = "jms/activeMqConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder builder = HealthCheckResponse.named("ActiveMq");

        try (Connection connection = connectionFactory.createConnection()) {
            if (connection == null) {
                return builder.down().withData("message", "No connection could be established.").build();
            }
            connection.start();
        } catch (JMSException e) {
            return builder.down().withData("message", e.getMessage()).build();
        }
        return builder.up().build();
    }
}
