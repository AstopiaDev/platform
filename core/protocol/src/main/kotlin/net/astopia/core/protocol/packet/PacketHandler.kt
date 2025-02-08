package net.astopia.core.protocol.packet

import com.google.protobuf.MessageLite
import io.netty.channel.ChannelHandlerContext

interface PacketHandler<T : MessageLite> {
    fun handle(ctx: ChannelHandlerContext, packet: T)
}