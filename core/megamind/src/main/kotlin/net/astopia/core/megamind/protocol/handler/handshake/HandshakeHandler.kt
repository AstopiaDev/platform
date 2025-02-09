package net.astopia.core.megamind.protocol.handler.handshake

import com.google.protobuf.MessageLite
import io.netty.channel.ChannelHandlerContext
import net.astopia.core.megamind.protocol.handler.handshake.processor.HandshakeProcessor
import net.astopia.core.proto.Protocol
import net.astopia.core.protocol.packet.HandlerRegistry
import net.astopia.core.protocol.packet.PacketHandler

class HandshakeHandler : PacketHandler<Protocol.HandshakeRequest> {
    companion object {
        val token: String = System.getenv("HANDSHAKE_TOKEN")
    }

    override fun handle(
        ctx: ChannelHandlerContext,
        packet: Protocol.HandshakeRequest,
        propagation: HandlerRegistry.PropagationContext,
        responder: (MessageLite) -> Unit
    ) {
        if (packet.token != token) {
            println("Invalid Handshake Request! ${ctx.channel().remoteAddress()}")
            ctx.channel().close()
            return
        }

        HandshakeProcessor.Registry.handle(ctx, packet, propagation)
        responder(Protocol.HandshakeResponse.newBuilder().setSuccess(true).build())
    }
}