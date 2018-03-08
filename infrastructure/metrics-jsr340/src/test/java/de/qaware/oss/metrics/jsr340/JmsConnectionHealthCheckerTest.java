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

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JmsConnectionHealthCheckerTest {

    @Mock
    private ConnectionFactory connectionFactory;

    private JmsConnectionHealthChecker healthChecker = new JmsConnectionHealthChecker();

    @Test
    public void checkHealthy() throws Exception {
        Connection connectionMock = Mockito.mock(Connection.class);
        when(connectionFactory.createConnection()).thenReturn(connectionMock);
        assertThat(healthChecker.check(connectionFactory).isHealthy(), is(true));
    }

    @Test
    public void checkUnhealthyNoConnection() {
        assertThat(healthChecker.check(connectionFactory).isHealthy(), is(false));
    }


    @Test
    public void checkUnhealthyJmsException() throws Exception {
        when(connectionFactory.createConnection()).thenThrow(new JMSException("failed"));
        assertThat(healthChecker.check(connectionFactory).isHealthy(), is(false));
    }

    @Test
    public void checkUnhealthyCloseConnection() throws Exception {
        Connection connectionMock = Mockito.mock(Connection.class);
        doThrow(new JMSException("")).when(connectionMock).close();
        when(connectionFactory.createConnection()).thenReturn(connectionMock);
        assertThat(healthChecker.check(connectionFactory).isHealthy(), is(false));
    }

    @Test
    public void checkUnhealthyExceptionOnStartConnection() throws Exception {
        Connection connectionMock = Mockito.mock(Connection.class);
        doThrow(new JMSException("")).when(connectionMock).start();
        when(connectionFactory.createConnection()).thenReturn(connectionMock);
        assertThat(healthChecker.check(connectionFactory).isHealthy(), is(false));
    }
}