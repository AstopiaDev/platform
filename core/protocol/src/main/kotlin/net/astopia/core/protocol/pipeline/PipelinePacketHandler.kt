package net.astopia.core.protocol.pipeline

import com.google.protobuf.MessageLite
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import net.astopia.core.protocol.packet.HandlerRegistry

class PipelinePacketHandler : SimpleChannelInboundHandler<MessageLite>() {
    override fun channelActive(ctx: ChannelHandlerContext?) {
        super.channelActive(ctx)
    }

    override fun channelInactive(ctx: ChannelHandlerContext?) {
        super.channelInactive(ctx)
    }

    override fun channelRead0(ctx: ChannelHandlerContext?, packet: MessageLite?) {
        if (ctx == null || packet == null) return
        HandlerRegistry.handle(packet, ctx)
    }
}