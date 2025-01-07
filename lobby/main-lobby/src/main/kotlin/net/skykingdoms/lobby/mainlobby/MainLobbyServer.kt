package net.skykingdoms.lobby.mainlobby

import net.minestom.server.event.player.PlayerSkinInitEvent
import net.skykingdoms.api.lobby.LobbyServer
import net.skykingdoms.api.lobby.hotbar.HotBar
import net.skykingdoms.api.shared.dummy.Dummy.Companion.DMM
import net.skykingdoms.api.shared.ui.sui.SideUi
import net.skykingdoms.api.shared.ui.tui.TabUi
import net.skykingdoms.api.shared.user.ServerUser
import net.skykingdoms.lobby.mainlobby.ui.sui.LobbySideUi

val tabUi = TabUi("Hello there<br>You are <player:name>", "<br>Hopefully you're enjoying your stay!")

object MainLobbyServer : LobbyServer() {
    override fun onStart() {
        super.onStart()

        SideUi.init(this)

        globalEventHandler.addListener(PlayerSkinInitEvent::class.java) { event ->
            val player = event.player as ServerUser

            tabUi.load(player)

            HotBar.get(0x0)?.load(player)
            player.sendMessage(DMM.invoke("hewo <player:name>", player))
        }
    }

    override fun defaultSideUi(loader: ServerUser): SideUi {
        return LobbySideUi(loader)
    }
}

fun main() {
    MainLobbyServer.start()
}