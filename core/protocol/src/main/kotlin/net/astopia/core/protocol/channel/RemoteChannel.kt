package net.astopia.core.protocol.channel

import io.netty.channel.socket.SocketChannel

interface RemoteChannel {
    val socketChannel: SocketChannel

    fun close() {
        if (!isActive()) {
            return
        }
        socketChannel.close()
    }

    fun isActive(): Boolean {
        return socketChannel.isActive
    }
}