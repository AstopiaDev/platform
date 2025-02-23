package net.astopia.core.protocol.nats.service

import com.google.protobuf.MessageLite
import io.nats.service.ServiceMessage
import io.nats.service.ServiceMessageHandler
import net.astopia.core.proto.Protocol.Packet
import net.astopia.core.protocol.registry.MessageRegistry

interface CustomMessageHandler<T : MessageLite> : ServiceMessageHandler {
    override fun onMessage(msg: ServiceMessage?) {
        val packet: Packet = Packet.parseFrom(msg?.data)
        if (msg != null) {
            onMsg(msg, MessageRegistry.parseMessage(packet.type, packet.data.toByteArray()) as T)
        }
    }

    fun onMsg(originalMsg: ServiceMessage, msg: T)
}