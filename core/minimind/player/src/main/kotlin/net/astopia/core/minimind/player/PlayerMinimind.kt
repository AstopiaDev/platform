package net.astopia.core.minimind.player

import net.astopia.core.minimind.player.connection.LocalClientConnection
import net.astopia.core.minimind.player.protocol.ProtocolPlayerMinimind
import net.astopia.core.proto.PlayerProtocol
import net.astopia.core.protocol.packet.PacketRegistry
import java.net.InetSocketAddress

object PlayerMinimind : ProtocolPlayerMinimind() {
    var connection: LocalClientConnection? = null

    fun init() {
        PacketRegistry.register("PlayerDataRequest", PlayerProtocol.PlayerDataRequest.parser())
        PacketRegistry.register("PlayerDataResponse", PlayerProtocol.PlayerDataResponse.parser())

        connection = LocalClientConnection(InetSocketAddress("127.0.0.1", 3455), 2)
        connection?.connectSynchronized()
    }
}

fun main() {
    PlayerMinimind.init()
}