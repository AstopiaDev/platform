package net.astopia.core.minimind.player

import io.nats.service.ServiceEndpoint
import net.astopia.core.protocol.nats.service.NatsService

object PlayerMinimind : NatsService("player_minimind", "Returns player data in a request") {
    fun init() {

    }

    override fun getEndpoints(): Array<ServiceEndpoint> {
        return arrayOf(

        )
    }
}

fun main() {
    PlayerMinimind.init()
}