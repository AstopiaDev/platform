package net.astopia.core.minimind.proxy

import io.nats.service.ServiceEndpoint
import net.astopia.core.minimind.proxy.handler.ProxyJoinHandler
import net.astopia.core.minimind.proxy.handler.ProxyQuitHandler
import net.astopia.core.minimind.proxy.handler.ProxyRegistrationRequestHandler
import net.astopia.core.protocol.nats.service.NatsService

object ProxyMinimind : NatsService("proxy_minimind", "Returns proxy info in a request") {
    fun init() {

    }

    override fun getEndpoints(): Array<ServiceEndpoint> {
        return arrayOf(
            ServiceEndpoint.builder()
                .endpointName("proxy_join")
                .handler(ProxyJoinHandler())
                .group(group)
                .build(),

            ServiceEndpoint.builder()
                .endpointName("proxy_quit")
                .handler(ProxyQuitHandler())
                .group(group)
                .build(),

            ServiceEndpoint.builder()
                .endpointName("proxy_registration_request")
                .group(group)
                .handler(ProxyRegistrationRequestHandler())
                .build()
        )
    }
}

fun main() {
    ProxyMinimind.init()
}