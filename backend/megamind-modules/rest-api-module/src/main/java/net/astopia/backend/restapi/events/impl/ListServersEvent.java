package net.astopia.backend.restapi.events.impl;

import net.astopia.backend.restapi.events.BaseEvent;
import net.astopia.backend.restapi.events.Event;
import net.astopia.backend.restapi.events.EventsHandler;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

@Event("list_servers")
public class ListServersEvent extends BaseEvent {
    @Override
    public void handle(EventsHandler handler, Session session) throws IOException {

    }
}
