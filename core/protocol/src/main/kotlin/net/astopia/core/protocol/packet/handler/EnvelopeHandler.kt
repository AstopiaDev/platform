package net.astopia.core.protocol.packet.handler

import com.google.protobuf.MessageLite
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.SocketChannel
import net.astopia.core.proto.Protocol.Packet
import net.astopia.core.proto.Protocol.RequestEnvelope
import net.astopia.core.proto.Protocol.ResponseEnvelope
import net.astopia.core.protocol.packet.HandlerRegistry
import net.astopia.core.protocol.packet.HandlerRegistry.PropagationContext
import net.astopia.core.protocol.packet.PacketHandler
import net.astopia.core.protocol.packet.PacketRegistry

sealed class EnvelopeHandler {

    class Request : PacketHandler<RequestEnvelope> {
        override fun handle(
            ctx: ChannelHandlerContext,
            packet: RequestEnvelope,
            propagation: PropagationContext,
            responder: (MessageLite) -> Unit
        ) {
            val payload = packet.request
            println("Meow part 1")
            if (payload != null) {
                val wrappedResponder = { response: MessageLite ->
                    println("Responding as " + response.javaClass.simpleName)

                    val commonPacket: Packet = Packet.newBuilder()
                        .setType(response.javaClass.simpleName)
                        .setData(response.toByteString())
                        .build()

                    val responseEnvelope = ResponseEnvelope.newBuilder()
                        .setRequestId(packet.requestId)
                        .setResponse(commonPacket.toByteString())
                        .setDirect(if (packet.hasDirect()) packet.direct else true)
                        .build()

                    println("Response is " + responseEnvelope.direct)

                    responder(responseEnvelope)
                    HandlerRegistry.sendPacket(ctx.channel() as SocketChannel, responseEnvelope)
                }

                val commonPacket = Packet.parseFrom(payload)
                val packet: MessageLite = PacketRegistry.parsePacket(commonPacket.type, commonPacket.data.toByteArray())

                HandlerRegistry.dispatch(
                    ctx,
                    packet,
                    propagation,
                    wrappedResponder
                )
            }
        }
    }

    class Response : PacketHandler<ResponseEnvelope> {
        override fun handle(
            ctx: ChannelHandlerContext,
            packet: ResponseEnvelope,
            propagation: PropagationContext,
            responder: (MessageLite) -> Unit
        ) {
            println("A response came in!")
            HandlerRegistry.handleResponse(ctx, packet)
        }
    }
}
