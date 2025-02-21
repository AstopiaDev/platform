package net.astopia.core.minimind.player.connection

import io.netty.channel.socket.SocketChannel
import net.astopia.core.minimind.player.PlayerDataHandler
import net.astopia.core.proto.PlayerProtocol.PlayerDataRequest
import net.astopia.core.protocol.channel.LocalClientChannel
import net.astopia.core.protocol.channel.RemoteServerChannel
import net.astopia.core.protocol.packet.HandlerRegistry
import java.net.SocketAddress

class LocalClientConnection(socketAddress: SocketAddress, threads: Int) : LocalClientChannel(socketAddress, threads) {
    override fun newServerChannel(channel: SocketChannel?): RemoteServerChannel? {
        return RemoteServerConnection(this, channel!!)
    }

    init {
        HandlerRegistry.registerHandler(PlayerDataRequest::class, 100, PlayerDataHandler())
    }
}