package net.astopia.core.protocol.packet

import com.google.protobuf.MessageLite
import io.netty.channel.ChannelHandlerContext

object HandlerRegistry {
    private val handlers = mutableMapOf<Class<*>, MutableList<PacketHandler<*>>>()

    fun <T : MessageLite> registerHandler(packetClass: Class<T>, handler: PacketHandler<T>) {
        handlers.getOrPut(packetClass) { mutableListOf() }.add(handler)
    }

    fun handle(packet: MessageLite, ctx: ChannelHandlerContext) {
        val packetClass = packet::class.java
        handlers[packetClass]?.forEach { handler ->
            try {
                @Suppress("UNCHECKED_CAST")
                (handler as PacketHandler<MessageLite>).handle(ctx, packet)
            } catch (e: Exception) {
                println("Error processing packet: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}