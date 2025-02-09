package net.astopia.core.protocol.channel

import io.netty.channel.socket.SocketChannel

abstract class RemoteClientChannel(override val socketChannel: SocketChannel) : RemoteChannel {
}