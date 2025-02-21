package net.astopia.core.protocol.packet

import com.google.protobuf.MessageLite
import com.google.protobuf.Parser;
import net.astopia.core.proto.Protocol
import net.astopia.core.proto.Protocol.RequestEnvelope
import net.astopia.core.proto.Protocol.ResponseEnvelope
import net.astopia.core.protocol.packet.handler.EnvelopeHandler

object PacketRegistry {
    private val registry: HashMap<String, Parser<out MessageLite>> = hashMapOf(
        "RequestEnvelope" to RequestEnvelope.parser(),
        "ResponseEnvelope" to ResponseEnvelope.parser(),
        "HandshakeRequest" to Protocol.HandshakeRequest.parser(),
        "HandshakeResponse" to Protocol.HandshakeResponse.parser(),
    )

    fun register(name: String, parser: Parser<out MessageLite>) {
        registry[name] = parser
    }

    fun parsePacket(type: String, data: ByteArray): MessageLite {
        val parser = registry[type]
        if (parser == null) {
            throw IllegalArgumentException("Unknown packet type $type")
        }
        return parser.parseFrom(data)
    }

    init {
        HandlerRegistry.registerHandler(RequestEnvelope::class, 50, EnvelopeHandler.Request())
        HandlerRegistry.registerHandler(ResponseEnvelope::class, 50, EnvelopeHandler.Response())
    }
}