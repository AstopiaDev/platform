package net.astopia.core.megamind.manager

import net.astopia.core.minimind.Minimind

object MinimindManager {
    private val miniminds: HashMap<String, Minimind> = HashMap()

    fun addMinimind(minimind: Minimind) {
        miniminds[minimind.getId()] = minimind
    }

    fun removeMinimind(minimind: Minimind) {
        miniminds.remove(minimind.getId())
    }

    fun getMinimind(id: String): Minimind? {
        return miniminds[id]
    }
}