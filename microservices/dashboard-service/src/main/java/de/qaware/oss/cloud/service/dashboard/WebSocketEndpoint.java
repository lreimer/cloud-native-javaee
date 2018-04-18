package de.qaware.oss.cloud.service.dashboard;

import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/events")
public class WebSocketEndpoint {

    @Inject
    private Logger logger;

    @Inject
    private WebSocketHandler handler;

    @OnOpen
    public void onOpen(Session session) throws IOException {
        logger.info("onOpen WebSocket session.");
        handler.add(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        // do not do anything here
        logger.log(Level.FINE, "Received {0} on websocket.", message);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("onClose WebSocket session.");
        handler.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.log(Level.WARNING, "Error on websocket.", throwable);
        handler.remove(session);
    }

}
