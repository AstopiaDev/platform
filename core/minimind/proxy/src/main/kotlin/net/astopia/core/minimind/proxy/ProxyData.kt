package net.astopia.core.minimind.proxy

import net.astopia.core.proto.Protocol.GeoRegion

class ProxyData(val id: Int, val region: GeoRegion, val capacity: Int) {
    private val playingIds: IntArray = IntArray(capacity) { -1 }
    private var size = 0

    fun addPlayer(playerId: Int): Boolean {
        if (size >= capacity) return false
        playingIds[size] = playerId
        size++
        return true
    }

    fun removePlayer(playerId: Int): Boolean {
        for (i in 0 until size) {
            if (playingIds[i] == playerId) {
                playingIds[i] = playingIds[size - 1]
                playingIds[size - 1] = -1
                size--
                return true
            }
        }
        return false
    }

    fun getPlayers(): List<Int> {
        return playingIds.take(size)
    }
}
