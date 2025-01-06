package net.skykingdoms.api.shared.npc

import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.PlayerHand
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.PlayerEntityInteractEvent
import net.minestom.server.instance.Instance

abstract class Npc(type: EntityType, posInstance: Instance, pos: Pos, val interactAction: InteractAction? = null) : LivingEntity(type) {
    companion object {
        fun init(globalEventHandler: GlobalEventHandler) {
            globalEventHandler.addListener(PlayerEntityInteractEvent::class.java) { event ->
                if (event.hand == PlayerHand.OFF) {
                    return@addListener
                }

                val entity = event.target

                if (entity is Npc) {
                    entity.interactAction?.execute(event)
                }
            }
        }
    }

    init {
        setInstance(posInstance, pos).join()
    }
}