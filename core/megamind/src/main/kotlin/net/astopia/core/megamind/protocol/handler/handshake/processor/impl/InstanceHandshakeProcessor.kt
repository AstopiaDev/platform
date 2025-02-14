package net.astopia.core.megamind.protocol.handler.handshake.processor.impl

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.SocketChannel
import net.astopia.core.megamind.manager.ArenaHolderManager
import net.astopia.core.megamind.protocol.handler.handshake.processor.HandshakeProcessor
import net.astopia.core.megamind.protocol.instance.ArenaHolderInstance
import net.astopia.core.proto.Instance
import net.astopia.core.proto.Protocol
import net.astopia.core.protocol.packet.HandlerRegistry
import java.lang.Integer.parseInt

class InstanceHandshakeProcessor : HandshakeProcessor {
    override fun process(
        ctx: ChannelHandlerContext,
        request: Protocol.HandshakeRequest,
        propagation: HandlerRegistry.PropagationContext
    ) {
        val metadata = request.metadataMap
        val instanceType = metadata["instanceType"]

        if (instanceType == null) {
            println("Invalid Handshake Request! ${ctx.channel().remoteAddress()}")
            ctx.channel().close()
            return
        }

        val instance = ArenaHolderInstance(ctx.channel() as SocketChannel, parseInt(metadata["capacity"]))
        val id = ArenaHolderManager.addInstance(instance)

        HandlerRegistry.sendPacket(
            instance.socketChannel,
            Instance.UpdateData.newBuilder().setId(id).build()
        )
    }
}