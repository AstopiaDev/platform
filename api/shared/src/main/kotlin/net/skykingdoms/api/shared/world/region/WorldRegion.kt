package net.skykingdoms.api.shared.world.region

import net.minestom.server.coordinate.Pos

class WorldRegion(val id: String, val pos1: Pos, val pos2: Pos, val weight: Int = 0) : Region {
    override fun id(): String {
        return id
    }

    override fun pos1(): Pos {
        return pos1
    }

    override fun pos2(): Pos {
        return pos2
    }

    override fun containsPos(pos: Pos): Boolean {
        val minX = pos1.x()
        val minY = pos1.y()
        val minZ = pos1.z()
        val maxX = pos2.x()
        val maxY = pos2.y()
        val maxZ = pos2.z()

        return pos.x in minX..maxX && pos.y in minY..maxY && pos.z in minZ..maxZ
    }
}