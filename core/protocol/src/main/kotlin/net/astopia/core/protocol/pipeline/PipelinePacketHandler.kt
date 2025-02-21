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

    /*
    fixes the
    WARNING: An exceptionCaught() event was fired, and it reached at the tail of the pipeline. It usually means the last handler in the pipeline did not handle the exception.
    shit
     */
    @Deprecated("Deprecated in Java")
    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        if (ctx != null) {
            HandlerRegistry.handleDisconnect(ctx)
        }
        ctx?.close()
    }
}