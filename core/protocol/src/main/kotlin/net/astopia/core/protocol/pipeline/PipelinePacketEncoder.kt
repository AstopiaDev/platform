package net.astopia.core.protocol.pipeline

import com.google.protobuf.ByteString
import com.google.protobuf.MessageLite
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import net.astopia.core.proto.Protocol.Packet

class PipelinePacketEncoder : MessageToByteEncoder<MessageLite>() {
    override fun encode(ctx: ChannelHandlerContext?, msg: MessageLite, out: ByteBuf) {
        val liteMsg: MessageLite = msg
        val simpleName = liteMsg.javaClass.simpleName
        val bytes = liteMsg.toByteArray()

        val packet = Packet.newBuilder()
            .setType(simpleName)
            .setData(ByteString.copyFrom(bytes))
            .build()

        out.writeBytes(packet.toByteArray())
    }
}