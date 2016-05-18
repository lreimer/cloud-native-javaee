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
import com.netflix.loadbalancer.Server;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This JAX-RS client request filter performs client side load balancing using Ribbon.
 * We use the host information from the original URI as serviceID and perform a lookup
 * for the load balanced host:port information.
 *
 * @author lreimer
 */
public class RibbonClientRequestFilter implements ClientRequestFilter {

    private final ILoadBalancer loadBalancer;

    /**
     * Initialize filter instance with a Ribbon load balancer.
     *
     * @param loadBalancer the load balancer
     */
    public RibbonClientRequestFilter(ILoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        // get the load balanced server info
        Server server = loadBalancer.chooseServer(null);
        String host = server.getHost();
        int port = server.getPort();

        URI uri = requestContext.getUri();
        URI lbUri;
        try {
            lbUri = new URI(uri.getScheme(), uri.getUserInfo(), host, port,
                    uri.getPath(), uri.getQuery(), uri.getFragment());
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }

        requestContext.setUri(lbUri);
    }
}
