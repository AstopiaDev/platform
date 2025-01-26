package net.astopia.api.megamind.event.impl.gamer;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NonNull;
import net.astopia.api.megamind.gamer.IMegamindGamer;

/**
 * 
 */
@Getter
public final class GamerJoinEvent extends GamerEvent {
    public GamerJoinEvent(final IMegamindGamer player) {
        super(player);
    }

    @Override
    public GamerJoinEvent serialize(@NonNull ByteBuf out) {
        //this.getGamer().serialize(out);
        return this;
    }

    @Override
    public GamerJoinEvent deserialize(@NonNull ByteBuf in) {

/*
        this.setGamer(AbstractGamer.getGamer());
*/
        return this;
    }
}
