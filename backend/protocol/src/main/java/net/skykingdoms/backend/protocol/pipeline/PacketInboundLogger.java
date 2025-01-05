package net.skykingdoms.backend.protocol.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.skykingdoms.api.protocol.platform.Platform;
import net.skykingdoms.backend.protocol.packet.Packet;
import org.jetbrains.annotations.NotNull;

public final class PacketInboundLogger extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
        if (msg instanceof Packet<?>) {
            Platform.logger.debug("[{}] Inbound packet: {}", ctx.channel().remoteAddress(), msg);
        }

        super.channelRead(ctx, msg);
    }
}
