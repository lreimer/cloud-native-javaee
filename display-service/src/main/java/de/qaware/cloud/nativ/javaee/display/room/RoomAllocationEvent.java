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

import javax.json.Json;
import javax.json.JsonObject;

/**
 * The allocation event holds the name of the room and the current allocation.
 */
public class RoomAllocationEvent {

    private final String roomName;
    private final int allocation;

    /**
     * Initialize the event with the room name and current allocation.
     *
     * @param roomName   the room name
     * @param allocation the occupancy
     */
    public RoomAllocationEvent(String roomName, int allocation) {
        this.roomName = roomName;
        this.allocation = allocation;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getAllocation() {
        return allocation;
    }

    /**
     * Return this instances as JSON representation.
     *
     * @return the JSON representatipon
     */
    public String toJson() {
        JsonObject message = Json.createObjectBuilder()
                .add("roomName", roomName)
                .add("allocation", allocation)
                .build();
        return message.toString();
    }
}
