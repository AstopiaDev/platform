package net.astopia.core.minimind.proxy.handler

import io.nats.service.ServiceMessage
import net.astopia.core.protocol.nats.service.CustomMessageHandler
import net.astopia.core.proxy.proto.Proxy.ProxyRegistrationRequest

class ProxyRegistrationRequestHandler : CustomMessageHandler<ProxyRegistrationRequest> {
    override fun onMsg(originalMsg: ServiceMessage, msg: ProxyRegistrationRequest) {

    }
}