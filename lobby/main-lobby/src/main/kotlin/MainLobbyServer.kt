import net.minestom.server.entity.Player
import net.skykingdoms.api.shared.ui.sui.SideUi
import ui.sui.LobbySideUi

object MainLobbyServer : LobbyServer() {
    override fun onStart() {
        super.onStart()

        SideUi.init(this)

/*        globalEventHandler.addListener(PlayerChunkLoadEvent::class.java) { event ->
            ServerSelectGui(event.player)
        }*/
    }

    override fun defaultSideUi(loader: Player): SideUi? {
        return LobbySideUi(loader)
    }
}

fun main() {
    MainLobbyServer.start()
}