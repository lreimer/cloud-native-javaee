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

import javax.enterprise.event.Event

/**
 * Spock spec for the device event transmitter. Basically test that
 * the JSON serialization is working correctly.
 */
class DeviceEventTransmitterSpec extends Specification {

    DeviceEventTransmitter transmitter

    def setup() {
        transmitter = new DeviceEventTransmitter()
        transmitter.events = Mock(Event)
        transmitter.logger = Mock(Logger)
    }

    def "Check JSON serialization on firing event"() {
        when:
        transmitter.fire(23, '1234567890')

        then:
        1 * transmitter.events.fire('{"roomNr":23,"cardId":"1234567890"}')
        1 * transmitter.logger.debug(_, 23, '1234567890')
    }
}
