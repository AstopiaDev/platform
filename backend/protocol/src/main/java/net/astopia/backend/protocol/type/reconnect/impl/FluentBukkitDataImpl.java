package net.astopia.backend.protocol.type.reconnect.impl;

import io.netty.buffer.ByteBuf;
import lombok.*;
import lombok.experimental.FieldDefaults;
import net.astopia.api.protocol.server.GameType;
import net.astopia.api.protocol.server.ServerScaleType;
import net.astopia.backend.protocol.packet.PacketUtils;
import net.astopia.backend.protocol.type.reconnect.FluentData;


@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class FluentBukkitDataImpl implements FluentData {

    String serverName;
    String nodeName;
    ServerScaleType serverScaleType;
    GameType gameType;
    String host;
    int port;

    @Override
    public FluentBukkitDataImpl serialize(@NonNull ByteBuf out) {
        PacketUtils.writeString(out, serverName);
        PacketUtils.writeString(out, nodeName);
        PacketUtils.writeEnum(out, serverScaleType);
        PacketUtils.writeEnum(out, gameType);
        PacketUtils.writeString(out, host);
        out.writeInt(port);
        return this;
    }

    @Override
    public FluentBukkitDataImpl deserialize(@NonNull ByteBuf in) {
        this.serverName = PacketUtils.readString(in);
        this.nodeName = PacketUtils.readString(in);
        this.serverScaleType = PacketUtils.readEnum(in, ServerScaleType.class);
        this.gameType = PacketUtils.readEnum(in, GameType.class);
        this.host = PacketUtils.readString(in);
        this.port = in.readInt();
        return this;
    }
}
