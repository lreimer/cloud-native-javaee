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
