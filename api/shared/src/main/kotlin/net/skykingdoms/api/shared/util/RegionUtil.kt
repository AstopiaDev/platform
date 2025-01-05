package net.skykingdoms.api.shared.util

import net.minestom.server.coordinate.Pos
import net.skykingdoms.api.shared.world.region.Region
import net.skykingdoms.api.shared.world.region.WorldRegion

object RegionUtil {
    fun getRegionAt(pos: Pos): WorldRegion? {
        val regions = Region.get()
            .filter { it is WorldRegion && it.containsPos(pos) }
            .map { it as WorldRegion }
            .toSet()

        if (regions.isEmpty()) {
            return null
        }

        var highestWeightRegion: WorldRegion? = null

        for (region in regions) {
            if (highestWeightRegion == null) {
                highestWeightRegion = region
                continue
            }

            if (region.weight >= highestWeightRegion.weight) {
                highestWeightRegion = region
            }
        }

        return highestWeightRegion
    }
}