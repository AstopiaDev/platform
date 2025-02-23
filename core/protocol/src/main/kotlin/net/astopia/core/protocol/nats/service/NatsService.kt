package net.astopia.core.protocol.nats.service

import io.nats.service.Group
import io.nats.service.Service
import io.nats.service.ServiceEndpoint
import net.astopia.core.protocol.nats.NatsClient

abstract class NatsService(name: String, desc: String) {
    private val natsUrl = System.getenv("NATS_SERVER_URL") ?: "nats://127.0.0.1:4222"
    private var service: Service

    val group: Group = Group(name)

    init {
        NatsClient.connect(natsUrl)
        service = createService(name, desc)
        service.startService()
    }

    private fun createService(name: String, desc: String): Service {
        val serviceBuilder = Service.builder()
            .name(name)
            .version("1.0.0")
            .description(desc)
            .connection(NatsClient.connection)

        for (endpoint in getEndpoints()) {
            serviceBuilder.addServiceEndpoint(endpoint)
        }

        return serviceBuilder.build()
    }

    abstract fun getEndpoints(): Array<ServiceEndpoint>
}