package net.astopia.backend.protocol.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import net.astopia.api.protocol.platform.Platform;
import net.astopia.backend.protocol.packet.PacketDirection;
import net.astopia.backend.protocol.packet.PacketProtocol;
import net.astopia.backend.protocol.packet.PacketUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PacketDecoder extends ByteToMessageDecoder {
    final PacketDirection direction;
    PacketProtocol state = PacketProtocol.HANDSHAKE;

    public void upgradeConnection(final @NonNull PacketProtocol newState) {
        state = newState;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, @NotNull ByteBuf buf, List<Object> out) {
        final String remoteAddress = ctx.channel().remoteAddress() != null
                ? ctx.channel().remoteAddress().toString()
                : "unknown-ip";

        if (buf.readableBytes() == 0) {
            Platform.logger.warn("Empty buffer from: " + remoteAddress);
            return;
        }

        int id = PacketUtils.readVarInt(buf);

        try {
            val packet = direction.getMapper(state).newPacket(id);

            if (packet == null) {
                Platform.logger.error("Unknown packet ID {} from {}", id, remoteAddress);
                return;
            }

            packet.read(buf);
            out.add(packet);

            Platform.logger.debug("Decoded packet {} from {}", id, remoteAddress);
        } catch (Exception ex) {
            Platform.logger.error(
                    "Error decoding packet {} from {}: {}",
                    id,
                    remoteAddress,
                    ex.getMessage()
            );
        }
    }
}
