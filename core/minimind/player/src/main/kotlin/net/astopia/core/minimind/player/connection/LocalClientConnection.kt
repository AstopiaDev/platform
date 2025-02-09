package net.astopia.core.minimind.player.connection

import io.netty.channel.socket.SocketChannel
import net.astopia.core.protocol.channel.LocalClientChannel
import net.astopia.core.protocol.channel.RemoteServerChannel
import java.net.SocketAddress

class LocalClientConnection(socketAddress: SocketAddress, threads: Int) : LocalClientChannel(socketAddress, threads) {
    override fun newServerChannel(channel: SocketChannel?): RemoteServerChannel? {
        return RemoteServerConnection(this, channel!!)
    }

}