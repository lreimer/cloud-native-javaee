/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 QAware GmbH, Munich, Germany
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.qaware.oss.metrics.jsr340;

import com.codahale.metrics.health.HealthCheck.Result;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Health check for a {@link DataSource}<br/>
 * Try to get a connection to the database to make sure it is still available
 * <p/>
 * The dataSource can be injected like this:
 * <pre>
 * \@Resource(lookup = "jdbc/ProcessDb")
 * private DataSource dataSource;
 * </pre>
 */
public class DataSourceHealthChecker {

    /**
     * Check the health status of given data source
     *
     * @param dataSource the data source to check
     * @return the health status result
     */
    public Result check(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            if (connection == null) {
                return Result.unhealthy("No database connection could be established");
            }
            if (!connection.isValid(1)) {
                return Result.unhealthy("Connection is not valid");
            }
        } catch (SQLException e) {
            return Result.unhealthy(e);
        }
        return Result.healthy();
    }
}
