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
package de.qaware.cloud.nativ.javaee.display.room

import org.slf4j.Logger
import spock.lang.Specification

import javax.enterprise.event.Event

/**
 * The Spock spec for the room allocation.
 */
class RoomAllocationSpec extends Specification {

    RoomAllocation allocation

    def setup() {
        allocation = new RoomAllocation()
        allocation.logger = Mock(Logger)
        allocation.roomEvent = Mock(Event)
    }

    def "Check empty allocation size"() {
        expect:
        allocation.getAllocation(1) == 0
    }

    def "Test update once"() {
        when:
        allocation.update(1, '2357')

        then:
        allocation.getAllocation(1) == 1
        1 * allocation.logger.info(_, '2357', '1')
        1 * allocation.roomEvent.fire({ it.getAllocation() == 1 })
    }

    def "Test update twice"() {
        when:
        allocation.update(1, '2357')
        allocation.update(1, '2357')

        then:
        allocation.getAllocation(1) == 0
        2 * allocation.logger.info(_, '2357', '1')
        2 * allocation.roomEvent.fire(_ as RoomAllocationEvent)
    }
}
