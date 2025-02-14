package net.astopia.core.protocol.channel.initializer

import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelPipelineException
import io.netty.channel.socket.SocketChannel
import net.astopia.core.protocol.channel.LocalChannel
import net.astopia.core.protocol.pipeline.PipelinePacketDecoder
import net.astopia.core.protocol.pipeline.PipelinePacketEncoder
import net.astopia.core.protocol.pipeline.PipelinePacketHandler

class CustomProtocolInitializer(val localChannel: LocalChannel) : ChannelInitializer<SocketChannel>() {
    override fun initChannel(socketChannel: SocketChannel?) {
        val pipeline = socketChannel?.pipeline() ?: throw ChannelPipelineException("Channel pipeline is null")

        //pipeline.addLast("frameDecoder", ProtobufVarint32FrameDecoder())
        pipeline.addLast("protobufDecoder", PipelinePacketDecoder())
        pipeline.addLast("protobufEncoder", PipelinePacketEncoder())
        pipeline.addLast("packetHandler", PipelinePacketHandler(localChannel, socketChannel))
    }
}