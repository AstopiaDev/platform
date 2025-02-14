package net.astopia.core.megamind.manager

import net.astopia.core.megamind.protocol.instance.ArenaHolderInstance
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.atomic.AtomicLong

object ArenaHolderManager {
    private val instancesById: ConcurrentMap<Long, ArenaHolderInstance> = ConcurrentHashMap()
    private val idGenerator = AtomicLong(1)

    fun addInstance(instance: ArenaHolderInstance): Long {
        val id = idGenerator.getAndIncrement()
        instancesById[id] = instance
        return id
    }

    fun getInstance(id: Long): ArenaHolderInstance? {
        return instancesById[id]
    }

    fun removeInstance(id: Long) {
        instancesById.remove(id)
    }
}
