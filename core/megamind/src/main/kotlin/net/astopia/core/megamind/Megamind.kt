package net.astopia.core.megamind

import net.astopia.core.megamind.protocol.LocalServerConnection
import net.astopia.core.proto.Protocol.RequestEnvelope
import net.astopia.core.protocol.packet.HandlerRegistry
import net.astopia.core.megamind.protocol.handler.envelope.MegamindEnvelopeHandler
import net.astopia.core.megamind.protocol.handler.handshake.HandshakeHandler
import net.astopia.core.proto.Protocol
import java.net.BindException
import java.net.InetSocketAddress

object Megamind {
    var connection: LocalServerConnection? = null

    fun init() {
        HandlerRegistry.registerHandler(Protocol.HandshakeRequest::class, 50, HandshakeHandler())
        HandlerRegistry.registerHandler(RequestEnvelope::class, 100, MegamindEnvelopeHandler.Request())

        connection = LocalServerConnection(InetSocketAddress("127.0.0.1", 3455), 2)
        try {
            connection!!.bindSynchronized()
        } catch (e: BindException) {
            e.printStackTrace()
            return
        }
    }
}

fun main() {
    Megamind.init()
}