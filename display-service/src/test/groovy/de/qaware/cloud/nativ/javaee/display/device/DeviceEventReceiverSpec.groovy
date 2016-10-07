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
package de.qaware.cloud.nativ.javaee.display.device

import de.qaware.cloud.nativ.javaee.common.api.DeviceEvent
import de.qaware.cloud.nativ.javaee.display.room.RoomAllocation
import org.slf4j.Logger
import spock.lang.Specification

/**
 * Test spec for the device event receiver implementation.
 */
class DeviceEventReceiverSpec extends Specification {

    DeviceEventReceiver receiver

    def setup() {
        receiver = new DeviceEventReceiver()
        receiver.logger = Mock(Logger)
        receiver.roomAllocation = Mock(RoomAllocation)
    }

    def "Test receiving of Device events"() {
        when:
        receiver.receive(new DeviceEvent(235, '1234567890'))

        then:
        1 * receiver.logger.info(_, 235, '1234567890')
        1 * receiver.roomAllocation.update(235, '1234567890')
    }
}
