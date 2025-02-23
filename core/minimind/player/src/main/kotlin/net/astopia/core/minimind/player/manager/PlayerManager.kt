package net.astopia.core.minimind.player.manager

import net.astopia.core.minimind.player.protocol.PlayerData
import java.util.concurrent.ConcurrentHashMap

object PlayerManager {
    private val players = ConcurrentHashMap<Int, PlayerData>()

    fun addPlayer(id: Int, player: PlayerData) {
        players[id] = player
    }

    fun removePlayer(id: Int) {
        players.remove(id)
    }

    fun getPlayer(id: Int): PlayerData? {
        return players[id]
    }
}