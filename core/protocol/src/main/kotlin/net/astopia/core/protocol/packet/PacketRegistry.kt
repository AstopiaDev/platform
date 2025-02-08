package net.astopia.core.protocol.packet

import com.google.protobuf.MessageLite
import com.google.protobuf.Parser;
import net.astopia.core.proto.Protocol

object PacketRegistry {
    private val registry: HashMap<String, Parser<out MessageLite>> = hashMapOf(
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
}