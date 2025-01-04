import LobbyServer
import net.minestom.server.entity.Player
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerChunkLoadEvent
import net.skykingdoms.api.shared.ui.sui.SideUi
import ui.gui.ServerSelectGui

object MainLobbyServer : LobbyServer() {
    override fun onStart() {
        super.onStart()

        SideUi.init(this)

        globalEventHandler.addListener(PlayerChunkLoadEvent::class.java) { event ->
            ServerSelectGui(event.player)
        }
    }

    override fun defaultSideUi(loader: Player): SideUi? {
        return super.defaultSideUi(loader)
    }
}

fun main() {
    MainLobbyServer.start()
}