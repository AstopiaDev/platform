package net.astopia.backend.restapi.events;

import net.astopia.api.protocol.platform.Platform;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EventsManager {
    private static final Map<String, Class<? extends BaseEvent>> eventTypes = new HashMap<>();

    static {
        Reflections reflections = new Reflections("net.skykingdoms.backend.restapi.events.impl");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Event.class);

        for (Class<?> clazz : annotated) {
            if (!BaseEvent.class.isAssignableFrom(clazz)) {
                continue;
            }

            Event event = clazz.getAnnotation(Event.class);
            eventTypes.put(event.value(), clazz.asSubclass(BaseEvent.class));

            Platform.logger.info("[RestAPI] Registered {} event", event.value());
        }
    }

    public static Class<? extends BaseEvent> getEventClass(String event) {
        return eventTypes.get(event);
    }
}
