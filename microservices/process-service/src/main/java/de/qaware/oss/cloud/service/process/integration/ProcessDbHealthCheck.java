package de.qaware.oss.cloud.service.process.integration;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Health check for the database
 * </p>
 * Try to get a connection to the database to make sure it is still available
 */
@ApplicationScoped
@Health
public class ProcessDbHealthCheck implements HealthCheck {

    @Resource(lookup = "jdbc/ProcessDb")
    private DataSource dataSource;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder builder = HealthCheckResponse.named("ProcessDb");

        try (Connection connection = dataSource.getConnection()) {
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
