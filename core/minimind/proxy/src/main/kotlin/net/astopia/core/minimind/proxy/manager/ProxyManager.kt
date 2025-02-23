package net.astopia.core.minimind.proxy.manager

import net.astopia.core.minimind.proxy.ProxyData
import net.astopia.core.proto.Protocol.GeoRegion
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

object ProxyManager {
    private val proxiesById = ConcurrentHashMap<Int, ProxyData>()
    private val idCounter = AtomicInteger()

    fun addProxy(data: ProxyData) {
        proxiesById[data.id] = data
    }

    fun getProxy(id: Int): ProxyData? {
        return proxiesById[id]
    }

    fun getProxiesByRegion(region: GeoRegion): List<ProxyData> {
        return proxiesById.values.filter { it.region == region }
    }

    fun removeProxy(id: Int) {
        proxiesById.remove(id)
    }
}
