package net.skykingdoms.api.shared.util

import net.minestom.server.coordinate.Pos

object LocationUtil {
    fun strToPos(str: String): Pos {
        val pos: List<String> = str.split(";")

        if (pos.size > 4) {
            return Pos(pos[0].toDouble(), pos[1].toDouble(), pos[2].toDouble(), pos[3].toFloat(), pos[4].toFloat())
        }

        return Pos(pos[0].toDouble(), pos[1].toDouble(), pos[2].toDouble())
    }

    fun chunkPos(x: Int, z: Int): Long {
        return (x.toLong() shl 32) or (z.toLong() and 0xFFFFFFFFL)
    }
}