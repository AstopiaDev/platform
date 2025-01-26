package net.astopia.backend.megamind.plugin.event;

import lombok.NonNull;
import lombok.val;
import net.astopia.api.megamind.event.Event;
import net.astopia.api.megamind.event.IEventBus;
import net.astopia.api.protocol.platform.Platform;
import net.astopia.backend.megamind.manager.ServiceManager;
import net.astopia.backend.megamind.server.impl.ServiceServer;
import net.astopia.backend.protocol.packet.play.PostEventPacket;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventBus implements IEventBus {
    Map<Class<?>, Set<String>> bySubscribedServices = new HashMap<>();

    @Override
    public void onPost(@NonNull Event event) {
        Set<String> subscribedServices = bySubscribedServices.get(event.getClass());
        if (subscribedServices != null) {
            for (val serviceName : subscribedServices) {
                ServiceServer service = ServiceManager.INSTANCE.getService(serviceName);

                if (service == null) {
                    continue;
                }

                service.getChannel().sendPacket(new PostEventPacket(event.getClass(), event));
                Platform.logger.info("Sending a packet to service {}", serviceName);
            }
        }
    }

    public void register(final @NonNull String service, final @NonNull Class<? extends Event>[] events) {
        for (Class<? extends Event> eventClazz : events) {
            bySubscribedServices
                    .computeIfAbsent(eventClazz, k -> new HashSet<>())
                    .add(service);
        }
    }

    public void unregister(final @NonNull String service) {
        for (Set<String> services : bySubscribedServices.values()) {
            services.remove(service);
        }
    }
}
