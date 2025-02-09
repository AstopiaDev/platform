package net.astopia.core.minimind.player.connection

import io.netty.channel.socket.SocketChannel
import net.astopia.core.proto.Protocol.HandshakeRequest
import net.astopia.core.proto.Protocol.HandshakeResponse
import net.astopia.core.protocol.channel.LocalClientChannel
import net.astopia.core.protocol.channel.RemoteServerChannel
import net.astopia.core.protocol.packet.HandlerRegistry

class RemoteServerConnection(localChannel: LocalClientChannel, socketChannel: SocketChannel) : RemoteServerChannel(localChannel, socketChannel) {
    companion object {
        val token: String = System.getenv("HANDSHAKE_TOKEN")
    }

    override fun onConnect() {
        HandlerRegistry.sendRequest(
            socketChannel,
            HandshakeRequest.newBuilder()
                .setToken(token)
                .setType("minimind")
                .putMetadata("name", "player")
                .build()
        ).thenAccept { resp ->
            val response: HandshakeResponse = resp as HandshakeResponse
            if (response.success) {
                println("Connected to Megamind!")
            } else {
                println("Failed to connect to Megamind: ${response.message}")
            }
        }
    }

    override fun onDisconnect() {

    }
}