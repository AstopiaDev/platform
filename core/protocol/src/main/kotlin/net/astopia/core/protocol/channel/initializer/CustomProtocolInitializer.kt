package net.astopia.core.protocol.channel.initializer

import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelPipelineException
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder
import net.astopia.core.protocol.pipeline.PipelinePacketDecoder
import net.astopia.core.protocol.pipeline.PipelinePacketEncoder

class CustomProtocolInitializer : ChannelInitializer<SocketChannel>() {
    override fun initChannel(channel: SocketChannel?) {
        val pipeline = channel?.pipeline()

        if (pipeline == null) {
            throw ChannelPipelineException("Channel pipeline is null")
        }

        pipeline.addLast("frameDecoder", ProtobufVarint32FrameDecoder())
        pipeline.addLast("protobufEncoder", PipelinePacketEncoder())
        pipeline.addLast("protobufDecoder", PipelinePacketDecoder())
    }
}