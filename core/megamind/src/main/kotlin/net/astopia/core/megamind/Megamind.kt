package net.astopia.core.megamind

import net.astopia.core.protocol.nats.NatsClient

object Megamind {
    fun init() {
        NatsClient.connect("nats://127.0.0.1:4222")
    }
}

fun main() {
    Megamind.init()
}