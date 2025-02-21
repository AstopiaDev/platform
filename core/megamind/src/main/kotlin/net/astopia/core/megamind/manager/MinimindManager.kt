package net.astopia.core.megamind.manager

import net.astopia.core.minimind.protocol.RemoteMinimind

object MinimindManager {
    private val miniminds: HashMap<String, RemoteMinimind> = HashMap()

    fun addMinimind(minimind: RemoteMinimind) {
        miniminds[minimind.getId()] = minimind
    }

    fun removeMinimind(minimind: RemoteMinimind) {
        miniminds.remove(minimind.getId())
    }

    fun getMinimind(id: String): RemoteMinimind? {
        return miniminds[id]
    }
}