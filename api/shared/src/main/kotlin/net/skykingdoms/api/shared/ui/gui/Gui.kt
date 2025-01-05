package net.skykingdoms.api.shared.ui.gui

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.skykingdoms.api.shared.ui.UserInterface

abstract class Gui(
    var loader: Player,
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

                val gui = inventory
                val slot = event.slot

                if (slot < 0 || slot >= gui.clickActions.size) {
                    if (!gui.allowMove) {
                        event.isCancelled = true
                    }
                    return@addListener
                }

                val clickAction = gui.clickActions[slot]
                if (clickAction == null && !gui.allowMove) {
                    event.isCancelled = true
                    return@addListener
                }

                clickAction?.execute(event)
            }

            globalEventHandler.addChild(guiNode)
        }
    }

    init {
        loader.openInventory(this)
    }

    fun item(slot: Int, item: ItemStack, clickAction: ClickAction?) {
        clickActions[slot] = clickAction
        this.setItemStack(slot, item)
    }

    fun item(slot: Int, item: ItemStack) {
        item(slot, item, null)
    }
}