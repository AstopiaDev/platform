package net.astopia.core.protocol.pipeline

import com.google.protobuf.ByteString
import com.google.protobuf.MessageLite
import com.google.protobuf.MessageLiteOrBuilder
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.protobuf.ProtobufEncoder
import net.astopia.core.proto.Common

class PipelinePacketEncoder : ProtobufEncoder() {
    override fun encode(ctx: ChannelHandlerContext?, msg: MessageLiteOrBuilder?, out: MutableList<Any?>?) {
        if (msg !is MessageLite) {
            throw IllegalArgumentException("Message must be a MessageLite")
            return
        }

        val liteMsg: MessageLite = msg
        val simpleName = liteMsg.javaClass.simpleName
        val bytes = liteMsg.toByteArray()

        val packet = Common.Packet.newBuilder()
            .setType(simpleName)
            .setData(ByteString.copyFrom(bytes))
            .build()

        out?.add(packet)
    }
}