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
package de.qaware.cloud.nativ.javaee.room.api;

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
 * A dummy room repository that contains some static rooms.
 */
@ApplicationScoped
public class DummyRoomRepository implements RoomRepository {

    private final ConcurrentMap<Integer, Room> rooms = new ConcurrentHashMap<>();

    private final Logger logger;

    @Inject
    public DummyRoomRepository(Logger logger) {
        this.logger = logger;
    }

    @PostConstruct
    public void initialize() {
        rooms.putIfAbsent(1, new Room("Alan Turing", 1));
        rooms.putIfAbsent(2, new Room("Kurt Gödel", 2));
        rooms.putIfAbsent(3, new Room("Bertram Russel", 3));
        rooms.putIfAbsent(4, new Room("Ada Lovelace", 4));
    }

    @Override
    public Collection<Room> all() {
        logger.debug("Returning all dummy rooms.");
        return Collections.unmodifiableCollection(rooms.values());
    }

    @Override
    public Optional<Room> byRoomNr(int roomNr) {
        logger.debug("Find dummy room by Nr={}.", roomNr);
        return Optional.ofNullable(rooms.get(roomNr));
    }
}
