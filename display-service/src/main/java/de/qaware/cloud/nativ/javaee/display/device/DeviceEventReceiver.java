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
package de.qaware.cloud.nativ.javaee.display.device;

import de.qaware.cloud.nativ.javaee.common.api.DeviceEvent;
import de.qaware.cloud.nativ.javaee.display.room.RoomAllocation;
import fish.payara.micro.cdi.Inbound;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * This is the receiver class for DeviceEvents sent by the Device service.
 * We will count how many people are in the room.
 */
@ApplicationScoped
public class DeviceEventReceiver {

    @Inject
    private Logger logger;

    @Inject
    private RoomAllocation roomAllocation;

    /**
     * Event receiver for DeviceEvents. Will process the incoming event JSON structure
     * and update the room allocation.
     *
     * @param event the device event
     */
    public void receive(@Observes @Inbound DeviceEvent event) {
        logger.info("Received DeviceEvent for roomNr={} and cardId={}.", event.getRoomNr(), event.getCardId());
        roomAllocation.update(event.getRoomNr(), event.getCardId());
    }

    void setLogger(Logger logger) {
        this.logger = logger;
    }

    void setRoomAllocation(RoomAllocation roomAllocation) {
        this.roomAllocation = roomAllocation;
    }
}
