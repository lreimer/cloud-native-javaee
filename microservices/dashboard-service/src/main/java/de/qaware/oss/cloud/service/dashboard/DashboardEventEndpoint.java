package de.qaware.oss.cloud.service.dashboard;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.Json;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@ServerEndpoint("/events")
public class DashboardEventEndpoint {

    @Inject
    private Logger logger;

    private Set<Session> sessions = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        // do not do anything here
        logger.log(Level.FINE, "Received {0} on websocket.", message);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.log(Level.WARNING, "Error on websocket.", throwable);
        sessions.remove(session);
    }

    public void broadcast(@Observes DashboardEvent event) {
        sessions.forEach(session -> {
            synchronized (session) {
                try {
                    Writer sendWriter = session.getBasicRemote().getSendWriter();
                    Json.createWriter(sendWriter).writeObject(event.toJson());
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Error writing event to websocket.", e);
                }
            }
        });
    }
}
