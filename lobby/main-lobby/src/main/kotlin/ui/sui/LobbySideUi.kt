package ui.sui

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.skykingdoms.api.shared.ui.sui.SideUi

class LobbySideUi(val loader: Player) : SideUi(loader, Component.text("Lobby")) {
    override fun draw() {
        line(Component.text("Just Testing!"), 2)
        line(Component.empty(), 1)
        line(Component.text("Hello player!"), 0)
    }
}