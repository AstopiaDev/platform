package net.astopia.core.minimind.proxy.handler

import io.nats.service.ServiceMessage
import net.astopia.core.minimind.proxy.manager.ProxyManager
import net.astopia.core.protocol.nats.service.CustomMessageHandler
import net.astopia.core.proxy.proto.Proxy.ProxyQuit

class ProxyQuitHandler : CustomMessageHandler<ProxyQuit> {
    override fun onMsg(originalMsg: ServiceMessage, msg: ProxyQuit) {
        ProxyManager.getProxy(msg.proxyId)?.removePlayer(msg.playerId)
    }
}