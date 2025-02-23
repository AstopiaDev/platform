package net.astopia.core.minimind.player.handler

import io.nats.service.ServiceMessage
import net.astopia.core.player.proto.Player.PlayerLoginRequest
import net.astopia.core.protocol.nats.service.CustomMessageHandler

class PlayerLoginRequestHandler : CustomMessageHandler<PlayerLoginRequest> {
    override fun onMsg(originalMsg: ServiceMessage, msg: PlayerLoginRequest) {
        
    }
}