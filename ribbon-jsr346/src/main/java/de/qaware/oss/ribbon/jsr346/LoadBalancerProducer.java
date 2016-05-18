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
package de.qaware.oss.ribbon.jsr346;

import com.netflix.loadbalancer.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.ArrayList;

import static de.qaware.oss.ribbon.jsr346.LoadBalancer.Type;

/**
 * A CDI producer bean implementation for Ribbon load balancers. Currently this implementation
 * supports Fixed and Dynamic types.
 *
 * @author lreimer
 */
@ApplicationScoped
public class LoadBalancerProducer {

    @Inject
    @Any
    private Instance<IRule> rules;

    @Inject
    @Any
    private Instance<IPing> pings;

    /**
     * Creates a load balancer instance using a fixed server list. You need to add the servers
     * to this instance manually after retrieving it. This instance has Dependent scope, so you will get
     * a fresh instance on every injection point.
     *
     * @return a fixed server list load balancer
     */
    @Produces
    @LoadBalancer(Type.Fixed)
    @Dependent
    public BaseLoadBalancer fixedServerListLoadBalancer() {
        LoadBalancerBuilder<Server> builder = LoadBalancerBuilder.newBuilder();

        LoadBalancer qualifier = new FixedLoadBalancerQualifier();
        IRule rule = select(rules, qualifier);
        if (rule != null) {
            builder.withRule(rule);
        }

        IPing ping = select(pings, qualifier);
        if (ping != null) {
            builder.withPing(ping);
        }

        return builder.buildFixedServerListLoadBalancer(new ArrayList<>());
    }

    /**
     * Creates a load balancer instance using a dynamic server list. You need to add the dynamic
     * servers to this instance manually after retrieving it. This instance has Dependent scope, so you
     * will get a fresh instance on every injection point.
     *
     * @return a dynamic server list loader balancer
     */
    @Produces
    @LoadBalancer(Type.Dynamic)
    @Dependent
    public ZoneAwareLoadBalancer<Server> dynamicServerListLoadBalancer() {
        LoadBalancerBuilder<Server> builder = LoadBalancerBuilder.newBuilder();

        LoadBalancer qualifier = new DynamicLoadBalancerQualifier();
        IRule rule = select(rules, qualifier);
        if (rule != null) {
            builder.withRule(rule);
        }

        IPing ping = select(pings, qualifier);
        if (ping != null) {
            builder.withPing(ping);
        }

        return builder.buildDynamicServerListLoadBalancer();
    }

    private <T> T select(Instance<T> instance, Annotation qualifier) {
        Instance<T> select = instance.select(qualifier);
        if (select.isUnsatisfied()) {
            // then try to find a default instance
            select = instance.select(new DefaultQualifier());
            if (select.isUnsatisfied()) {
                return null;
            }
            return select.get();
        }
        return select.get();
    }

    /**
     * An annotation literal implementation of the {@link LoadBalancer} annotation for programmatic lookup
     * of fixed server list balancer instance.
     */
    private static class FixedLoadBalancerQualifier extends AnnotationLiteral<LoadBalancer> implements LoadBalancer {
        @Override
        public Type value() {
            return Type.Fixed;
        }
    }

    /**
     * An annotation literal implementation of the {@link LoadBalancer} annotation for programmatic lookup
     * of dynamic server list balancer instance.
     */
    private static class DynamicLoadBalancerQualifier extends AnnotationLiteral<LoadBalancer> implements LoadBalancer {
        @Override
        public Type value() {
            return Type.Dynamic;
        }
    }

    /**
     * The annotation literal class for the CDI {@link Default} qualifier annotation.
     */
    private static class DefaultQualifier extends AnnotationLiteral<Default> implements Default {
    }
}
