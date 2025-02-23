package net.astopia.core.protocol.serialization

import com.google.protobuf.MessageLite
import net.astopia.core.proto.Protocol.Packet
import net.astopia.core.protocol.registry.MessageRegistry

class ProtoSerializer {
    inline fun <reified T : MessageLite> serialize(message: T): ByteArray {
        println("Serializing message with the classname ${T::class.java.simpleName}")
        return Packet.newBuilder()
            .setType(T::class.java.simpleName)
            .setData(message.toByteString())
            .build()
            .toByteArray()
    }

    inline fun <reified T : MessageLite> deserialize(data: ByteArray): T {
        val message = Packet.parseFrom(data)
        return MessageRegistry.parseMessage(message.type, message.data.toByteArray()) as T
    }
}