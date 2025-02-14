package net.astopia.core.megamind.protocol.instance

import io.netty.channel.socket.SocketChannel
import net.astopia.core.protocol.channel.RemoteChannel

class ArenaHolderInstance(override val socketChannel: SocketChannel, capacity: Int) : PlayableInstance(capacity), RemoteChannel {
}