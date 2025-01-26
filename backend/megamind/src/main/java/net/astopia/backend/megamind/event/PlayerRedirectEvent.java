package net.astopia.backend.megamind.event;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.astopia.api.megamind.event.Cancellable;
import net.astopia.api.megamind.event.Event;
import net.astopia.backend.megamind.data.gamer.Gamer;
import net.astopia.backend.megamind.server.AbstractPlayerHolder;

/**
 * 
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public final class PlayerRedirectEvent extends Event implements Cancellable {

    final Gamer gamer;

    final AbstractPlayerHolder target;

    @Getter @Setter private boolean cancelled;

    public PlayerRedirectEvent(final @NonNull Gamer gamer,
                               final @NonNull AbstractPlayerHolder target) {
        this.gamer = gamer;
        this.target = target;
    }

    @Override
    public Event serialize(@NonNull ByteBuf out) {
        return null;
    }

    @Override
    public Event deserialize(@NonNull ByteBuf in) {
        return null;
    }
}