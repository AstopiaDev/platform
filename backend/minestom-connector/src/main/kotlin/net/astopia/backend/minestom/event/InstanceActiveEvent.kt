package net.astopia.backend.minestom.event

import net.minestom.server.event.Event
import net.astopia.backend.protocol.packet.BossProcessor

class InstanceActiveEvent(private val processor: BossProcessor) : Event {
    fun processor() : BossProcessor {
        return processor
    }
}