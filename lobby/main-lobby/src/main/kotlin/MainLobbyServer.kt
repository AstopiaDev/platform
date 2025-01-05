import net.minestom.server.entity.Player
import net.skykingdoms.api.shared.ui.sui.SideUi
import net.skykingdoms.api.shared.user.ServerUser
import ui.sui.LobbySideUi

object MainLobbyServer : LobbyServer() {
    override fun onStart() {
        super.onStart()

        SideUi.init(this)
    }

    override fun defaultSideUi(loader: ServerUser): SideUi? {
        return LobbySideUi(loader)
    }
}

fun main() {
    MainLobbyServer.start()
}