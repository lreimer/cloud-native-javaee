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
package de.qaware.cloud.nativ.javaee.device.api

import org.slf4j.Logger
import spock.lang.Specification
import spock.lang.Unroll

/**
 * A simple Spock specification to test the dummy repository.
 */
class DummyDeviceRepositorySpec extends Specification {

    DummyDeviceRepository repository
    Logger logger

    void setup() {
        logger = Mock(Logger)
        repository = new DummyDeviceRepository(logger)
        repository.initialize()
    }

    def "Find all dummy devices."() {
        expect:
        repository.all().size() == 4
    }

    @Unroll
    def "Find device by ID #deviceId"() {
        expect:
        repository.byDeviceId(deviceId).isPresent() == found

        where:
        deviceId || found
        "1234"   || true
        "0815"   || true
        "4711"   || true
        "????"   || false
    }
}
