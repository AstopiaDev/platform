package net.skykingdoms.api.shared.ui.gui.type

import net.kyori.adventure.text.Component
import net.minestom.server.inventory.InventoryType
import net.skykingdoms.api.shared.ui.gui.Gui
import net.skykingdoms.api.shared.user.ServerUser

abstract class ChestGui(loader: ServerUser, rows: Int, title: Component, allowMove: Boolean) : Gui(
    loader,
    rowMap[rows] ?: throw IllegalArgumentException("Invalid number of rows: $rows"),
    title,
    arrayOfNulls(rows * 9),
    allowMove
) {
    companion object {
        val rowMap: HashMap<Int, InventoryType> = hashMapOf(
            1 to InventoryType.CHEST_1_ROW,
            2 to InventoryType.CHEST_2_ROW,
            3 to InventoryType.CHEST_3_ROW,
            4 to InventoryType.CHEST_4_ROW,
            5 to InventoryType.CHEST_5_ROW,
            6 to InventoryType.CHEST_6_ROW,
        )
    }
}
