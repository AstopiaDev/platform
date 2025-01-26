package net.astopia.backend.minestom.event

import net.minestom.server.event.Event
import net.astopia.backend.protocol.type.reconnect.impl.FluentBukkitDataImpl

class InstanceDataUpdateEvent(private val data: FluentBukkitDataImpl) : Event {
    fun data() : FluentBukkitDataImpl {
        return data
    }
}