package ui.sui

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.skykingdoms.api.shared.ui.sui.SideUi

class LobbySideUi(loader: Player) : SideUi(loader, Component.text("Lobby").font(Key.key("arbor", "hud"))) {
    override fun draw() {
        line(Component.text("Just Testing!").font(Key.key("arbor", "hud")), 2)
        line(Component.empty(), 1)
        line(Component.text("Hello player!").font(Key.key("arbor", "hud")), 0)
    }
}