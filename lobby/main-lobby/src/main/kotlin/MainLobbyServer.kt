import hotbar.HotBar
import net.minestom.server.event.player.PlayerRespawnEvent
import net.skykingdoms.api.shared.ui.sui.SideUi
import net.skykingdoms.api.shared.user.ServerUser
import ui.sui.LobbySideUi

object MainLobbyServer : LobbyServer() {
    override fun onStart() {
        super.onStart()

        SideUi.init(this)

        globalEventHandler.addListener(PlayerRespawnEvent::class.java) { event ->
            val player = event.player as ServerUser
            HotBar.get(0x0)?.load(player)
        }
    }

    override fun defaultSideUi(loader: ServerUser): SideUi {
        return LobbySideUi(loader)
    }
}

fun main() {
    MainLobbyServer.start()
}