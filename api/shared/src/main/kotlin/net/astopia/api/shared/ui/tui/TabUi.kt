package net.astopia.api.shared.ui.tui

import net.kyori.adventure.text.Component
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.astopia.api.shared.dummy.Dummy.Companion.DMM
import net.astopia.api.shared.ui.UserInterface
import net.astopia.api.shared.user.ServerUser
import net.astopia.backend.minestom.HeartBeat
import java.util.concurrent.ConcurrentHashMap

// TODO: implement player language support
//  implement updating based on events
open class TabUi(private val header: String, private val footer: String) : UserInterface {
    companion object {
        private val players: ConcurrentHashMap<ServerUser, TabUi> = ConcurrentHashMap()

        fun init(globalEventHandler: GlobalEventHandler) {
            globalEventHandler.addListener(PlayerDisconnectEvent::class.java) { event ->
                val player = event.player as ServerUser
                unload(player)
            }

            HeartBeat.runTimerAsync({
                for (player in players.keys) {
                    val tabUi = players[player]
                    tabUi?.refresh(player)
                }
            }, 1, 20)
        }

        private fun unload(player: ServerUser) {
            players.remove(player)
        }
    }

    fun load(loader: ServerUser) {
        players[loader] = this
        refresh(loader)
    }

    fun refresh(loader: ServerUser) {
        val translatedHeader: Component = DMM.invoke(header, loader)
        val translatedFooter: Component = DMM.invoke(footer, loader)
        loader.sendPlayerListHeaderAndFooter(translatedHeader, translatedFooter)
    }

    override fun draw() {

    }
}