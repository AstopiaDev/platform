/*
package net.astopia.core.megamind.protocol.handler.handshake.processor.impl

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.SocketChannel
import net.astopia.core.megamind.manager.MinimindManager
import net.astopia.core.megamind.protocol.handler.handshake.processor.HandshakeProcessor
import net.astopia.core.minimind.protocol.RemoteMinimind
import net.astopia.core.proto.Protocol
import net.astopia.core.protocol.packet.HandlerRegistry

class MinimindHandshakeProcessor : HandshakeProcessor {
    override fun process(
        ctx: ChannelHandlerContext,
        request: Protocol.HandshakeRequest,
        propagation: HandlerRegistry.PropagationContext
    ) {
        val metadata = request.metadataMap
        val name = metadata["name"]

        if (name == null) {
            println("Invalid Handshake Request! ${ctx.channel().remoteAddress()}")
            ctx.channel().close()
            return
        }

        val minimind = RemoteMinimind(name, ctx.channel() as SocketChannel)
        MinimindManager.addMinimind(minimind)
        println("Registered new Minimind: " + minimind.getId())
    }
}*/
