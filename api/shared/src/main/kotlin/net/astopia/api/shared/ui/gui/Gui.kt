package net.astopia.api.shared.ui.gui

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.astopia.api.shared.ui.Modernized
import net.astopia.api.shared.ui.UserInterface
import net.astopia.api.shared.user.ServerUser

abstract class Gui(
    var loader: ServerUser,
    invType: InventoryType,
    title: Component,
    var clickActions: Array<ClickAction?>,
    var allowMove: Boolean = false
) : Inventory(invType, title), UserInterface {
    companion object {
        fun init(globalEventHandler: GlobalEventHandler) {
            val guiNode = EventNode.type("gui-click", EventFilter.INVENTORY) { event, inventory ->
                inventory is Gui
            }

            guiNode.addListener(InventoryPreClickEvent::class.java) { event ->
                val player = event.player
                val inventory = event.inventory

                if (inventory !is Gui) return@addListener

                val slot = event.slot

                if (slot < 0 || slot >= inventory.clickActions.size) {
                    if (!inventory.allowMove) {
                        event.isCancelled = true
                    }
                    return@addListener
                }

                val clickAction = inventory.clickActions[slot]
                if (clickAction == null && !inventory.allowMove) {
                    event.isCancelled = true
                    return@addListener
                }

                clickAction?.execute(event)
            }

            globalEventHandler.addChild(guiNode)
        }
    }

    init {
        if (loader.isModern() && this is Modernized<*>) {
            modernize()
        } else {
            loader.openInventory(this)
        }
    }

    fun item(slot: Int, item: ItemStack, clickAction: ClickAction? = null) {
        if (slot >= 0 && slot < clickActions.size) {
            clickActions[slot] = clickAction
            setItemStack(slot, item)
        }
    }
}