package net.astopia.core.minimind.proxy.handler

import io.nats.service.ServiceMessage
import net.astopia.core.minimind.proxy.manager.ProxyManager
import net.astopia.core.protocol.nats.service.CustomMessageHandler
import net.astopia.core.proxy.proto.Proxy.ProxyJoin

class ProxyJoinHandler : CustomMessageHandler<ProxyJoin> {
    override fun onMsg(originalMsg: ServiceMessage, msg: ProxyJoin) {
        ProxyManager.getProxy(msg.proxyId)?.addPlayer(msg.playerId)
    }
}