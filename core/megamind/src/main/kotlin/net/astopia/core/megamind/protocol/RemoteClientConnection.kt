package net.astopia.core.megamind.protocol

import io.netty.channel.socket.SocketChannel
import net.astopia.core.megamind.protocol.handler.handshake.HandshakeHandler
import net.astopia.core.proto.Protocol
import net.astopia.core.protocol.channel.RemoteClientChannel
import net.astopia.core.protocol.packet.HandlerRegistry

class RemoteClientConnection(socketChannel: SocketChannel) : RemoteClientChannel(socketChannel) {
    init {
        HandlerRegistry.registerHandler(Protocol.HandshakeRequest::class, 1, HandshakeHandler())

        HandlerRegistry.onConnect(1) { ctx ->
            println("Connected!!")
        }
    }

}