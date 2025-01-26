package net.astopia.api.megamind.event.impl.gamer;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.astopia.api.megamind.event.Event;
import net.astopia.api.megamind.gamer.IMegamindGamer;

/**
 * 
 */
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public abstract class GamerEvent extends Event {
    @NonFinal
    IMegamindGamer gamer;
}
