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
package de.qaware.oss.ribbon.jsr339;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.LoadBalancerBuilder;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.ClientRequestContext;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Basic unit test to check the load balancing behaviour of the request filter.
 */
@RunWith(MockitoJUnitRunner.class)
public class RibbonClientRequestFilterTest {

    private ILoadBalancer loadBalancer;
    private RibbonClientRequestFilter filter;

    @Mock
    private ClientRequestContext requestContext;

    @Before
    public void setUp() throws Exception {
        Server server1 = new Server("host1", 4711);
        Server server2 = new Server("host2", 4712);

        List<Server> servers = Arrays.asList(server1, server2);
        loadBalancer = LoadBalancerBuilder.newBuilder()
                .withRule(new RoundRobinRule())
                .buildFixedServerListLoadBalancer(servers);
        filter = new RibbonClientRequestFilter(loadBalancer);
    }

    @Test
    public void filter() throws Exception {
        URI uri = new URI("https://user:password@qaware.de:433/test/test.html?q=test");
        when(requestContext.getUri()).thenReturn(uri);

        filter.filter(requestContext);

        ArgumentCaptor<URI> captor = ArgumentCaptor.forClass(URI.class);
        verify(requestContext).setUri(captor.capture());
        URI lbUri = captor.getValue();
        assertThat(lbUri.toString(), is("https://user:password@host2:4712/test/test.html?q=test"));

        reset(requestContext);
        when(requestContext.getUri()).thenReturn(uri);

        filter.filter(requestContext);

        captor = ArgumentCaptor.forClass(URI.class);
        verify(requestContext).setUri(captor.capture());
        lbUri = captor.getValue();
        assertThat(lbUri.toString(), is("https://user:password@host1:4711/test/test.html?q=test"));
    }

}