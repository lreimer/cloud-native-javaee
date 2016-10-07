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
package de.qaware.cloud.nativ.javaee.display.room;

import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * The room allocation keeps track which cards is in which room. It basically
 * checks if a cards is already in a room, if not the card enters the room and
 * otherwise the card leaves the room.
 */
@ApplicationScoped
public class RoomAllocation {

    private final Map<String, ConcurrentSkipListSet<String>> allocation = new ConcurrentHashMap<>();

    @Inject
    private Logger logger;

    @Inject
    private Event<RoomAllocationEvent> roomEvent;

    /**
     * Updates the room allocation for the given room number and card ID.
     *
     * @param roomNr the room number
     * @param cardId the card ID
     */
    public void update(int roomNr, String cardId) {
        // TODO replace with lookup of room name
        String roomName = getRoomName(roomNr);

        ConcurrentSkipListSet<String> cards = new ConcurrentSkipListSet<>();
        cards.add(cardId);

        cards = allocation.putIfAbsent(roomName, cards);
        if (cards != null && !cards.add(cardId)) {
            // if the add was not successful, then the cardId is
            // already in the set -> we need to remove the card
            cards.remove(cardId);
            logger.info("Card {} left the room {}.", cardId, roomName);
        } else {
            logger.info("Card {} entered the room {}.", cardId, roomName);
        }

        roomEvent.fire(new RoomAllocationEvent(roomName, allocation.get(roomName).size()));
    }

    private String getRoomName(int roomNr) {
        return Integer.toString(roomNr);
    }

    public int getAllocation(int roomNr) {
        return allocation.getOrDefault(getRoomName(roomNr), new ConcurrentSkipListSet<>()).size();
    }

    void setLogger(Logger logger) {
        this.logger = logger;
    }

    void setRoomEvent(Event<RoomAllocationEvent> roomEvent) {
        this.roomEvent = roomEvent;
    }
}
