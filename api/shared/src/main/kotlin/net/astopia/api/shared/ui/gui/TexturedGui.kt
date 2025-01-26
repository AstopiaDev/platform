package net.astopia.api.shared.ui.gui

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.inventory.InventoryType
import net.astopia.api.shared.user.ServerUser

abstract class TexturedGui(
    loader: ServerUser,
    invType: InventoryType,
    header: Component,
    icon: Component,
    title: Component,
    footer: Component,
    clickActions: Array<ClickAction?>,
    allowMove: Boolean
) : Gui(loader, invType, header.append(icon).append(title).append(footer), clickActions, allowMove) {
}