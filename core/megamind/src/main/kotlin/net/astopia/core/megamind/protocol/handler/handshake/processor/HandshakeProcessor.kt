package net.astopia.core.megamind.protocol.handler.handshake.processor

import io.netty.channel.ChannelHandlerContext
import net.astopia.core.megamind.protocol.handler.handshake.processor.impl.MinimindHandshakeProcessor
import net.astopia.core.proto.Protocol.HandshakeRequest
import net.astopia.core.protocol.packet.HandlerRegistry.PropagationContext

interface HandshakeProcessor {
    fun process(
        ctx: ChannelHandlerContext,
        request: HandshakeRequest,
        propagation: PropagationContext
    )

    object Registry {
        val processors: Map<String, HandshakeProcessor> = mapOf(
            "minimind" to MinimindHandshakeProcessor(),
        )

        fun handle(ctx: ChannelHandlerContext, request: HandshakeRequest, propagation: PropagationContext) {
            processors[request.type]?.process(ctx, request, propagation)
        }
    }
}