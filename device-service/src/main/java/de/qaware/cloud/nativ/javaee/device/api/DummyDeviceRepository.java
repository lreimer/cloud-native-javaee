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
package de.qaware.cloud.nativ.javaee.device.api;

import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A dummy device repository that contains some static devices.
 */
@ApplicationScoped
public class DummyDeviceRepository implements DeviceRepository {

    private final ConcurrentMap<String, Device> devices = new ConcurrentHashMap<>();

    private final Logger logger;

    @Inject
    public DummyDeviceRepository(final Logger logger) {
        this.logger = logger;
    }

    @PostConstruct
    public void initialize() {
        devices.putIfAbsent("0815", new Device("0815", 1));
        devices.putIfAbsent("4711", new Device("4711", 2));
        devices.putIfAbsent("2305", new Device("2305", 3));
        devices.putIfAbsent("1234", new Device("1234", 4));
    }

    @Override
    public Collection<Device> all() {
        logger.debug("Returning all dummy devices.");
        return Collections.unmodifiableCollection(devices.values());
    }

    @Override
    public Optional<Device> byDeviceId(String deviceId) {
        logger.debug("Find dummy device by ID={}.", deviceId);
        return Optional.ofNullable(devices.get(deviceId));
    }
}
