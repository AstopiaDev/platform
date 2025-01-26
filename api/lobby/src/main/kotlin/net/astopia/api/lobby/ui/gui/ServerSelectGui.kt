package net.astopia.api.lobby.ui.gui

import net.astopia.api.shared.ui.gui.type.ChestGui
import net.kyori.adventure.text.Component
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.astopia.api.shared.ui.Modernized
import net.astopia.api.shared.ui.gui.ClickAction
import net.astopia.api.shared.ui.gui.Gui
import net.astopia.api.shared.user.ServerUser

class ServerSelectGui(loader: ServerUser) : ChestGui(loader, 6, Component.text("Server Selector"), false), Modernized<Gui> {
    init {
        if (!loader.isModern()) {
            draw()
        }
    }

    override fun draw() {
        item(0, ItemStack.of(Material.STONE), object : ClickAction {
            override fun execute(event: InventoryPreClickEvent) {
                loader.sendMessage("Hello, you clicked the stone!")
                event.isCancelled = true
            }
        })
    }

    override fun modernize(): Gui {
        return Modern(loader)
    }

    private class Modern(loader: ServerUser) : ChestGui(loader, 6, Component.text("Server Selector"), false) {
        init {
            draw()
        }

        override fun draw() {
            item(1, ItemStack.of(Material.OAK_WOOD), object : ClickAction {
                override fun execute(event: InventoryPreClickEvent) {
                    loader.sendMessage("Hello, you clicked the oak wood!")
                    event.isCancelled = true
                }
            })
        }
    }
}