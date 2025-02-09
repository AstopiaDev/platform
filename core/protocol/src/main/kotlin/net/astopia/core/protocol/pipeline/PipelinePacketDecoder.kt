package net.astopia.core.protocol.pipeline

import com.google.protobuf.MessageLite
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.protobuf.ProtobufDecoder
import net.astopia.core.proto.Protocol.Packet
import net.astopia.core.protocol.packet.PacketRegistry

class PipelinePacketDecoder : ProtobufDecoder(Packet.getDefaultInstance()) {
    override fun decode(ctx: ChannelHandlerContext?, msg: ByteBuf?, out: MutableList<Any?>?) {
        if (msg == null || msg.readableBytes() == 0) {
            throw IllegalArgumentException("Empty packet")
        }

        val bytes = ByteArray(msg.readableBytes())
        msg.readBytes(bytes)

        val commonPacket: Packet = Packet.parseFrom(bytes)
        val packet: MessageLite = PacketRegistry.parsePacket(commonPacket.type, commonPacket.data.toByteArray())

        out?.add(packet)
    }
}
