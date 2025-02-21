package net.astopia.core.minimind.punishments.connection

import io.netty.channel.socket.SocketChannel
import net.astopia.core.proto.PlayerProtocol
import net.astopia.core.proto.PlayerProtocol.PlayerDataRequest
import net.astopia.core.proto.Protocol.*
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
                .putMetadata("name", "punishments")
                .build()
        ).thenAccept { resp ->
            val response: HandshakeResponse = resp as HandshakeResponse
            if (response.success) {
                println("Connected to Megamind!")
                HandlerRegistry.sendRequest(
                    socketChannel,
                    PlayerDataRequest.newBuilder()
                    .setUuid("YES")
                    .build(),
                    "player",
                    false
                ).thenAccept { dataResp ->
                    println("Recieved something cxD")
                    val dataResponse: PlayerProtocol.PlayerDataResponse = dataResp as PlayerProtocol.PlayerDataResponse
                    println("Recieved " + dataResponse.uuid)
                }
            } else {
                println("Failed to connect to Megamind: ${response.message}")
            }
        }
    }

    override fun onDisconnect() {

    }
}