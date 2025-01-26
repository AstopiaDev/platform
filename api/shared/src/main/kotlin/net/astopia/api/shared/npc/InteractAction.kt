package net.astopia.api.shared.npc

import net.minestom.server.event.player.PlayerEntityInteractEvent

fun interface InteractAction {
    fun execute(event: PlayerEntityInteractEvent)
}