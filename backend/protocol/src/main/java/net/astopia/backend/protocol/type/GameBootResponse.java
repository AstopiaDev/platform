package net.astopia.backend.protocol.type;

import io.netty.buffer.ByteBuf;
import lombok.*;
import lombok.experimental.FieldDefaults;
import net.astopia.api.protocol.packet.serialize.Serializable;
import net.astopia.api.protocol.type.GameMode;
import net.astopia.backend.protocol.packet.PacketUtils;

/**
 * @author Vihara
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public final class GameBootResponse implements Serializable<GameBootResponse> {

    /**
     * A game mode that we boot shiet
     */
    GameMode gameMode;

    /**
     * Amount of game modes
     */
    int amount;

    @Override
    public GameBootResponse serialize(@NonNull ByteBuf out) {
        PacketUtils.writeEnum(out, gameMode);
        out.writeInt(amount);
        return this;
    }

    @Override
    public GameBootResponse deserialize(@NonNull ByteBuf in) {
        this.gameMode = PacketUtils.readEnum(in, GameMode.class);
        this.amount = in.readInt();
        return this;
    }
}
