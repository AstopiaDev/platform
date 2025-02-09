package net.astopia.core.protocol.packet

import com.google.protobuf.MessageLite
import io.netty.channel.ChannelHandlerContext
import net.astopia.core.protocol.packet.HandlerRegistry.PropagationContext

interface PacketHandler<T : MessageLite> {
    fun handle(
        ctx: ChannelHandlerContext,
        packet: T,
        propagation: PropagationContext,
        responder: (MessageLite) -> Unit = {}
    )
}