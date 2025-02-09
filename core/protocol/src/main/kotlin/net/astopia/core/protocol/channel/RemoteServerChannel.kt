package net.astopia.core.protocol.channel

import io.netty.channel.socket.SocketChannel
import net.astopia.core.protocol.packet.HandlerRegistry

abstract class RemoteServerChannel(val localClientChannel: LocalClientChannel, override val socketChannel: SocketChannel) : RemoteChannel {
    init {
        HandlerRegistry.onDisconnect(1) { ctx ->
            if (!localClientChannel.isReconnecting()) {
                onDisconnect()
            }

            localClientChannel.reconnect()
        }

        HandlerRegistry.onConnect(1) { ctx ->
            onConnect()
        }
    }

    abstract fun onConnect()
    abstract fun onDisconnect()
}