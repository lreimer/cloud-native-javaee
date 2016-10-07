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
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * The nadler class for websocket session interactions.
 */
@ApplicationScoped
public class RoomAllocationSessionHandler {

    private final Set<Session> sessions = new HashSet<>();

    @Inject
    private Logger logger;

    /**
     * Add the session to the interal state of not already present.
     *
     * @param session the websocket session
     */
    public void add(final Session session) {
        logger.info("Websocket session {} connected.", session.getId());
        sessions.add(session);
    }

    /**
     * Remove the session from the interal state.
     *
     * @param session the websocket session
     */
    public void remove(final Session session) {
        logger.info("Websocket session {} disconnected.", session.getId());
        sessions.remove(session);
    }

    /**
     * Sends the Room allocation event to all registered sessions.
     *
     * @param event the allocation event
     */
    public void send(@Observes RoomAllocationEvent event) {
        String message = event.toJson();
        logger.info("Sending room allocation event {}.", message);

        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.warn("Unable to send message to session {}.", session.getId());
                sessions.remove(session);
            }
        }
    }

    void setLogger(Logger logger) {
        this.logger = logger;
    }
}
