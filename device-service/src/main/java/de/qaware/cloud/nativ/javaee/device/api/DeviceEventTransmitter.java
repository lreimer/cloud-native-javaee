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

import de.qaware.cloud.nativ.javaee.common.api.Qualified;
import fish.payara.micro.cdi.Outbound;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import java.io.StringWriter;

/**
 * The transmitter bean to fire outbound events. With CDI 2.0 we could make
 * them asynchronous, don't know with Payara outbound events.
 */
@ApplicationScoped
public class DeviceEventTransmitter {

    @Inject
    private Logger logger;

    @Inject
    @Qualified("DeviceEvent")
    @Outbound
    private Event<String> events;

    /**
     * Fire a devie event for the given room number and card ID.
     *
     * @param roomNr the room number
     * @param cardId the card ID
     */
    public void fire(int roomNr, String cardId) {
        logger.debug("Fire DeviceEvent for roomNr={} and cardId={}.", roomNr, cardId);
        events.fire(toJson(roomNr, cardId));
    }

    private String toJson(int roomNr, String cardId) {
        JsonObject event = Json.createObjectBuilder()
                .add("roomNr", roomNr)
                .add("cardId", cardId)
                .build();

        StringWriter writer = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(writer)) {
            jsonWriter.writeObject(event);
        }

        return writer.toString();
    }

    void setLogger(Logger logger) {
        this.logger = logger;
    }

    void setEvents(Event<String> events) {
        this.events = events;
    }
}
