package net.astopia.core.protocol.pipeline

import com.google.protobuf.MessageLite
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.socket.SocketChannel
import net.astopia.core.proto.Protocol.RequestEnvelope
import net.astopia.core.proto.Protocol.ResponseEnvelope
import net.astopia.core.protocol.channel.LocalChannel
import net.astopia.core.protocol.packet.HandlerRegistry
import net.astopia.core.protocol.packet.handler.EnvelopeHandler

class PipelinePacketHandler(localChannel: LocalChannel, socketChannel: SocketChannel) : SimpleChannelInboundHandler<MessageLite>() {
    init {
        localChannel.newRemoteChannel(socketChannel)

        HandlerRegistry.registerHandler(RequestEnvelope::class, 1, EnvelopeHandler.Request())
        HandlerRegistry.registerHandler(ResponseEnvelope::class, 1, EnvelopeHandler.Response())
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        HandlerRegistry.handleConnect(ctx)
        super.channelActive(ctx)
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        HandlerRegistry.handleDisconnect(ctx)
        super.channelInactive(ctx)
    }

    override fun channelRead0(ctx: ChannelHandlerContext, packet: MessageLite) {
        HandlerRegistry.handlePacket(ctx, packet)
    }
}