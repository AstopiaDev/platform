package net.skykingdoms.api.shared.ui.tui

import net.kyori.adventure.text.Component
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.skykingdoms.api.shared.user.ServerUser
import net.skykingdoms.backend.minestom.HeartBeat
import java.util.concurrent.ConcurrentHashMap

open class TabUi(private val header: Component, private val footer: Component) {
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
            }, 0, 20)
        }

        private fun unload(player: ServerUser) {
            players.remove(player)
        }
    }

    fun load(loader: ServerUser) {
        players[loader] = this
        refresh(loader)
    }

    // TODO: placeholder system for translating placeholders that are going to be in the tablist
    fun refresh(loader: ServerUser) {
        loader.sendPlayerListHeaderAndFooter(header, footer)
    }
}