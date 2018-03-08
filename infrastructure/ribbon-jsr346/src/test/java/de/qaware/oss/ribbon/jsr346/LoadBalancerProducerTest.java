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
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * A CDI unit test for the load balancer producer.
 *
 * @author lreimer
 */
@RunWith(CdiTestRunner.class)
public class LoadBalancerProducerTest {

    @Inject
    @LoadBalancer(LoadBalancer.Type.Fixed)
    private ILoadBalancer fixed;

    @Inject
    @LoadBalancer(LoadBalancer.Type.Dynamic)
    private ILoadBalancer dynamic;

    @Test
    public void fixedServerListLoadBalancer() throws Exception {
        assertThat(fixed, is(notNullValue()));

        IPing ping = ((BaseLoadBalancer) fixed).getPing();
        assertThat(ping, is(instanceOf(NoOpPing.class)));

        IRule rule = ((BaseLoadBalancer) fixed).getRule();
        assertThat(rule, is(instanceOf(RoundRobinRule.class)));
    }

    @Test
    public void dynamicServerListLoadBalancer() throws Exception {
        assertThat(dynamic, is(notNullValue()));

        IPing ping = ((ZoneAwareLoadBalancer) dynamic).getPing();
        assertThat(ping, is(instanceOf(DummyPing.class)));

        IRule rule = ((ZoneAwareLoadBalancer) dynamic).getRule();
        assertThat(rule, is(instanceOf(AvailabilityFilteringRule.class)));
    }

}