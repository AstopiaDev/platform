package net.skykingdoms.api.lobby.ui.gui

import net.skykingdoms.api.shared.ui.gui.type.ChestGui
import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.skykingdoms.api.shared.ui.gui.ClickAction

class ServerSelectGui(loader: Player) : ChestGui(loader, 6, Component.text("Server Selector"), false) {
    init {
        draw()
    }

    override fun draw() {
        item(0, ItemStack.of(Material.STONE), object : ClickAction {
            override fun execute(event: InventoryPreClickEvent) {
                loader.sendMessage("Hello, you clicked the stone!")
                event.isCancelled = true
            }
        })
    }
}