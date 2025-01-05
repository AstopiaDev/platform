package net.skykingdoms.api.shared.event

import net.minestom.server.entity.Player
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.trait.PlayerInstanceEvent
import net.skykingdoms.api.shared.GameServer
import net.skykingdoms.backend.minestom.HeartBeat

class PlayerJoinEvent(private val player: Player) : PlayerInstanceEvent {
    override fun getPlayer(): Player {
        return player
    }

    companion object {
        fun init(gameServer: GameServer) {
            gameServer.globalEventHandler
                .addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
                    HeartBeat.runLaterSync({
                        gameServer.globalEventHandler.call(PlayerJoinEvent(event.player))
                    }, 10)
                }
        }
    }
}