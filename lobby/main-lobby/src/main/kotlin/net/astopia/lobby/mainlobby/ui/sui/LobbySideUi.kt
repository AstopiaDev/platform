package net.astopia.lobby.mainlobby.ui.sui

import net.astopia.api.protocol.platform.Platform
import net.astopia.api.shared.ui.sui.SideUi
import net.astopia.api.shared.user.ServerUser

class LobbySideUi(loader: ServerUser) : SideUi(loader, loader.language.getMessageComponent("main_lobby_sidebar_title")) {
    override fun draw() {
        val lines = loader.language.getMessageComponentList("main_lobby_sidebar")

        Platform.logger.info("$lines amount of lines")

        var index = 0;
        for (line in lines.reversed()) {
            line(line, index)
            index++
        }
    }
}