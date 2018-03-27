package de.qaware.oss.cloud.service.payment.integration;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Health check for the database
 * </p>
 * Try to get a connection to the database to make sure it is still available
 */
@ApplicationScoped
@Health
public class PaymentDbHealthCheck implements HealthCheck {

    @Resource(lookup = "jdbc/PaymentDb")
    private DataSource dataSource;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder builder = HealthCheckResponse.named("PaymentDb");

        try (java.sql.Connection connection = dataSource.getConnection()) {
            if (connection == null) {
                return builder.down().withData("message", "No database connection could be established.").build();
            }
            if (!connection.isValid(1)) {
                return builder.down().withData("message", "Connection is not valid.").build();
            }
        } catch (SQLException e) {
            return builder.down().withData("message", e.getMessage()).build();
        }
        return builder.up().build();
    }
}
