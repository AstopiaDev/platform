package net.astopia.lobby.mainlobby

import net.minestom.server.event.player.PlayerSkinInitEvent
import net.astopia.api.lobby.LobbyServer
import net.astopia.api.lobby.hotbar.HotBar
import net.astopia.api.shared.dummy.Dummy.Companion.DMM
import net.astopia.api.shared.ui.sui.SideUi
import net.astopia.api.shared.ui.tui.TabUi
import net.astopia.api.shared.user.ServerUser
import net.astopia.lobby.mainlobby.ui.sui.LobbySideUi

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