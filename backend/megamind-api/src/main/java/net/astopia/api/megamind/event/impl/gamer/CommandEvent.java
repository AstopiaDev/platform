package net.astopia.api.megamind.event.impl.gamer;

import io.netty.buffer.ByteBuf;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.astopia.api.megamind.event.Cancellable;
import net.astopia.api.megamind.event.Event;
import net.astopia.api.megamind.gamer.IMegamindGamer;

/**
 * @author Vihara
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@ToString
@Getter
public final class CommandEvent extends Event implements Cancellable {

    IMegamindGamer sender;
    String command;
    String[] args;

    @Setter @NonFinal boolean cancelled;

    @Override
    public Event serialize(@NonNull ByteBuf out) {
        return null;
    }

    @Override
    public Event deserialize(@NonNull ByteBuf in) {
        return null;
    }
}
