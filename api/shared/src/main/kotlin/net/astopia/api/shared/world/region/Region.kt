package net.astopia.api.shared.world.region

import net.minestom.server.coordinate.Pos
import java.util.concurrent.ConcurrentHashMap

interface Region {
    fun id(): String
    fun pos1(): Pos
    fun pos2(): Pos
    fun containsPos(pos: Pos): Boolean

    companion object {
        private val regions: ConcurrentHashMap<String, Region> = ConcurrentHashMap()

        fun add(region: Region) {
            regions[region.id()] = region
        }

        fun remove(region: Region) {
            regions.remove(region.id())
        }

        fun get(region: String): Region? {
            return regions[region]
        }

        fun get() : Set<Region> {
            return regions.values.toSet()
        }
    }
}