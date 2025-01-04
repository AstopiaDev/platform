package net.skykingdoms.api.shared.ui.gui

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.inventory.InventoryType

abstract class TexturedGui(
    loader: Player,
    invType: InventoryType,
    header: Component,
    icon: Component,
    title: Component,
    footer: Component,
    clickActions: Array<ClickAction?>,
    allowMove: Boolean
) : Gui(loader, invType, header.append(icon).append(title).append(footer), clickActions, allowMove) {
}