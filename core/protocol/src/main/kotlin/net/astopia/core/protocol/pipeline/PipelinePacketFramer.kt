package net.astopia.core.protocol.pipeline

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageCodec
import io.netty.handler.codec.CorruptedFrameException
import net.astopia.core.protocol.util.PacketUtils

class PipelinePacketFramer : ByteToMessageCodec<ByteBuf>() {
    override fun encode(ctx: ChannelHandlerContext, from: ByteBuf, to: ByteBuf) {
        val packetSize = from.readableBytes()
        val headerSize = PacketUtils.getVarIntSize(packetSize)
        to.ensureWritable(headerSize + packetSize)
        PacketUtils.writeVarInt(to, packetSize)
        to.writeBytes(from, from.readerIndex(), packetSize)
    }

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        buf.markReaderIndex()

        if (!buf.isReadable) {
            return
        }

        try {
            val length = PacketUtils.readVarInt(buf)

            if (buf.readableBytes() < length) {
                buf.resetReaderIndex()
                return
            }

            out.add(buf.slice(buf.readerIndex(), length).retain())
            buf.skipBytes(length)
        } catch (e: Exception) {
            buf.resetReaderIndex()
            throw CorruptedFrameException("Failed to decode packet", e)
        }
    }
}