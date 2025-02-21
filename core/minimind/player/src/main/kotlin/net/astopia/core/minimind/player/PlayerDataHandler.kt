package net.astopia.core.minimind.player

import com.google.protobuf.MessageLite
import io.netty.channel.ChannelHandlerContext
import net.astopia.core.proto.PlayerProtocol.PlayerDataRequest
import net.astopia.core.proto.PlayerProtocol.PlayerDataResponse
import net.astopia.core.protocol.packet.HandlerRegistry
import net.astopia.core.protocol.packet.PacketHandler

class PlayerDataHandler : PacketHandler<PlayerDataRequest> {
    override fun handle(
        ctx: ChannelHandlerContext,
        packet: PlayerDataRequest,
        propagation: HandlerRegistry.PropagationContext,
        responder: (MessageLite) -> Unit
    ) {
        println("Recieved XD " + packet.uuid)
        responder(PlayerDataResponse.newBuilder().setUuid("LOLOLOL").build())
    }
}