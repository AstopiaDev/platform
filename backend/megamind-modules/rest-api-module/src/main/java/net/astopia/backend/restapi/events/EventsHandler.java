package net.astopia.backend.restapi.events;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import net.astopia.api.protocol.platform.Platform;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
@Getter
public class EventsHandler {
    private static Map<Session, String> sessions = new ConcurrentHashMap<>();
    private static Gson gson = new Gson();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        sessions.put(session, session.getRemoteAddress().toString());
        System.out.println("Client connected: " + session.getRemoteAddress());
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        sessions.remove(session);
        System.out.println("Client disconnected: " + session.getRemoteAddress());
    }
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            JsonObject jsonMessage = JsonParser.parseString(message).getAsJsonObject();
            String eventType = jsonMessage.get("event").getAsString();

            Class<? extends BaseEvent> eventClass = EventsManager.getEventClass(eventType);

            if (eventClass == null) {
                Platform.logger.warn("[RestAPI] Unknown WebSocket event: {}", eventType);
                return;
            }

            BaseEvent event = gson.fromJson(jsonMessage, eventClass);

            event.handle(this, session);
        } catch (Exception e) {
            Platform.logger.stacktrace(e, "[RestAPI] Error occurred on receiving WebSocket message: ");
        }
    }

    public void broadcastAll(BaseEvent event) throws IOException {
        String message = gson.toJson(event);
        for (Session s : sessions.keySet()) {
            if (s.isOpen()) {
                s.getRemote().sendString(message);
            }
        }
    }
}
