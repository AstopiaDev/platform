package net.astopia.api.shared.event

import net.minestom.server.event.Event

fun interface EventAction {
    fun execute(event: Event)
}