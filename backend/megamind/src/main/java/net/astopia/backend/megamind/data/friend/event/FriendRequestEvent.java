package net.astopia.backend.megamind.data.friend.event;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.astopia.api.megamind.event.Cancellable;
import net.astopia.api.megamind.event.Event;
import net.astopia.backend.megamind.data.gamer.Gamer;

/**
 * @author Vihara
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(callSuper = true)
@Data
public final class FriendRequestEvent extends Event implements Cancellable {

    Gamer sender;
    Gamer gamer;
    @NonFinal boolean cancelled;

    @Override
    public Event serialize(@NonNull ByteBuf out) {
        return null;
    }

    @Override
    public Event deserialize(@NonNull ByteBuf in) {
        return null;
    }
}
