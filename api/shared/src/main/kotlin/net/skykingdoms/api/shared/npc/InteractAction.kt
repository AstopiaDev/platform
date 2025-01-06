package net.skykingdoms.api.shared.npc

import net.minestom.server.event.player.PlayerEntityInteractEvent

fun interface InteractAction {
    fun execute(event: PlayerEntityInteractEvent)
}