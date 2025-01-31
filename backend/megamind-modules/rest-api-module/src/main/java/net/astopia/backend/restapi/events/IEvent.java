package net.astopia.backend.restapi.events;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public interface IEvent {
    void handle(EventsHandler handler, Session session) throws IOException;
}
