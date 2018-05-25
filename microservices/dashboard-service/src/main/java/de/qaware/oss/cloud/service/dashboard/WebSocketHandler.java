package de.qaware.oss.cloud.service.dashboard;

import io.opentracing.Span;
import io.opentracing.util.GlobalTracer;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class WebSocketHandler {

    @Inject
    private Logger logger;

    private Set<Session> sessions;

    @PostConstruct
    void initialize() {
        sessions = new CopyOnWriteArraySet<>();
    }

    public void broadcast(@Observes DashboardEvent event) {
        logger.info("Broadcasting event to sessions.");

        Span span = GlobalTracer.get().buildSpan("broadcast to all sessions").start();
        try {
            sessions.forEach(session -> {
                synchronized (session) {
                    broadcastToSession(event, span, session);
                }
            });
        } finally {
            span.finish();
        }
    }

    private void broadcastToSession(@Observes DashboardEvent event, Span span, Session session) {

        Span child = GlobalTracer.get().buildSpan("broadcast to session")
                .asChildOf(span)
                .start();
        try {
            String text = event.toJson().toString();
            session.getBasicRemote().sendText(text);
            logger.log(Level.INFO, "Broadcasting event {0} to session.", text);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error writing event to websocket.", e);
        } finally {
            child.finish();
        }
    }

    public void add(Session session) {
        sessions.add(session);
    }

    public void remove(Session session) {
        sessions.remove(session);
    }
}
