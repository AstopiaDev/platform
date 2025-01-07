package net.skykingdoms.api.lobby.hotbar.impl

import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.skykingdoms.api.lobby.hotbar.HotBar
import net.skykingdoms.api.shared.item.Item
import net.skykingdoms.api.shared.ui.gui.ClickAction

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