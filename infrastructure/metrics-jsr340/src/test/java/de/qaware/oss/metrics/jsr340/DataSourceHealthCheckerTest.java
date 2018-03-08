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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * Unit test for DataSourceHealthChecker
 */
@RunWith(MockitoJUnitRunner.class)
public class DataSourceHealthCheckerTest {

    @Mock
    private DataSource dataSource;

    private DataSourceHealthChecker healthChecker = new DataSourceHealthChecker();

    @Test
    public void checkHealthy() throws Exception {
        Connection connectionMock = Mockito.mock(Connection.class);
        when(connectionMock.isValid(anyInt())).thenReturn(true);
        when(dataSource.getConnection()).thenReturn(connectionMock);
        assertThat(healthChecker.check(dataSource).isHealthy(), is(true));
    }

    @Test
    public void checkUnhealthyCloseConnection() throws Exception {
        Connection connectionMock = Mockito.mock(Connection.class);
        doThrow(new SQLException()).when(connectionMock).close();
        when(connectionMock.isValid(anyInt())).thenReturn(true);
        when(dataSource.getConnection()).thenReturn(connectionMock);
        assertThat(healthChecker.check(dataSource).isHealthy(), is(false));
    }

    @Test
    public void checkUnhealthyNoConnection() {
        assertThat(healthChecker.check(dataSource).isHealthy(), is(false));
    }

    @Test
    public void checkUnhealthyConnectionInvalid() throws Exception {
        Connection connectionMock = Mockito.mock(Connection.class);
        when(connectionMock.isValid(anyInt())).thenReturn(false);
        when(dataSource.getConnection()).thenReturn(connectionMock);
        assertThat(healthChecker.check(dataSource).isHealthy(), is(false));
    }

    @Test
    public void checkUnhealthySqlException() throws Exception {
        doThrow(new SQLException()).when(dataSource).getConnection();
        assertThat(healthChecker.check(dataSource).isHealthy(), is(false));
    }

    @Test
    public void checkUnhealthyExceptionOnIsValid() throws Exception {
        Connection connectionMock = Mockito.mock(Connection.class);
        doThrow(new SQLException()).when(connectionMock).isValid(anyInt());
        when(dataSource.getConnection()).thenReturn(connectionMock);
        assertThat(healthChecker.check(dataSource).isHealthy(), is(false));
    }
}