package net.astopia.api.lobby.hotbar.impl

import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.astopia.api.lobby.hotbar.HotBar
import net.astopia.api.shared.item.Item
import net.astopia.api.shared.ui.gui.ClickAction

class LobbyHotBar : HotBar(
    items = arrayOf(
        Item(ItemStack.builder(Material.COMPASS).build())
    ),
    invClickActions = arrayOf(
        ClickAction { event ->
            event.isCancelled = true
        }
    )
)