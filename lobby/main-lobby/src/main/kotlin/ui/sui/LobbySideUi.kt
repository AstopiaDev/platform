package ui.sui

import net.skykingdoms.api.shared.ui.sui.SideUi
import net.skykingdoms.api.shared.user.ServerUser

class LobbySideUi(loader: ServerUser) : SideUi(loader, loader.language.getMessageComponent("main_lobby_sidebar_title")) {
    override fun draw() {
        val lines = loader.language.getMessageComponentList("main_lobby_sidebar")

        var index = 0;
        for (line in lines.reversed()) {
            line(line, index)
            index++
        }
    }
}