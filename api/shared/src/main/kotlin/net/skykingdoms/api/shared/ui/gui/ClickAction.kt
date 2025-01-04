package net.skykingdoms.api.shared.ui.gui

import net.minestom.server.event.inventory.InventoryPreClickEvent

interface ClickAction {
    fun execute(event: InventoryPreClickEvent)
}