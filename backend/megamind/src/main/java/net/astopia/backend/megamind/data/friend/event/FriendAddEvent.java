package net.astopia.backend.megamind.data.friend.event;

import io.netty.buffer.ByteBuf;
import lombok.*;
import lombok.experimental.FieldDefaults;
import net.astopia.api.megamind.event.Event;
import net.astopia.backend.megamind.data.gamer.Gamer;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class FriendAddEvent extends Event {

    Gamer sender;
    Gamer gamer;


    @Override
    public Event serialize(@NonNull ByteBuf out) {
        return null;
    }

    @Override
    public Event deserialize(@NonNull ByteBuf in) {
        return null;
    }
}
