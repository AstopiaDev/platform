package net.astopia.core.megamind.protocol.handler.envelope

import com.google.protobuf.MessageLite
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.SocketChannel
import net.astopia.core.megamind.manager.MinimindManager
import net.astopia.core.proto.Protocol.*
import net.astopia.core.protocol.packet.HandlerRegistry
import net.astopia.core.protocol.packet.HandlerRegistry.PropagationContext
import net.astopia.core.protocol.packet.PacketHandler

sealed class MegamindEnvelopeHandler {
    class Request : PacketHandler<RequestEnvelope> {
        override fun handle(
            ctx: ChannelHandlerContext,
            packet: RequestEnvelope,
            propagation: PropagationContext,
            responder: (MessageLite) -> Unit
        ) {
            val payload = packet.request
            val requestId = packet.requestId

            println("JUST MEOW")

            if (!packet.hasMinimind()) {
                return
            }

            propagation.stop()

            val minimindId = packet.minimind

            if (payload != null) {
                val minimind = MinimindManager.getMinimind(minimindId) ?: return

                println("MEOW PART 2")

                val wrappedResponder = { response: MessageLite ->
                    println("RESPONSE XDXDXD " + response.javaClass.simpleName)

                    val responseEnvelope = ResponseEnvelope.newBuilder()
                        .setRequestId(requestId)
                        .setResponse(response.toByteString())
                        .setDirect(true)
                        .build()

                    responder(responseEnvelope)
                    println("Sending packet witih request id " + requestId )
                    HandlerRegistry.sendPacket(ctx.channel() as SocketChannel, responseEnvelope)
                }

                HandlerRegistry.sendRequest(minimind.socketChannel, packet).thenAccept { response ->
                    println("MEOWIEEE " + response.javaClass.simpleName)

                    println("GOT A RESPONSE " )

                    wrappedResponder(response)
                }
            }
        }
    }
}
