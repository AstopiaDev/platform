package net.astopia.api.shared.event

import net.minestom.server.MinecraftServer
import net.minestom.server.event.Event
import net.minestom.server.event.GlobalEventHandler
import java.util.concurrent.ConcurrentHashMap

interface EventSubbed {
    fun subscribedTo(): List<Class<out Event>>

    companion object {
        private val listenerCache: ConcurrentHashMap<Class<out Event>, ArrayList<EventAction>> = ConcurrentHashMap()
        private val globalEventHandler: GlobalEventHandler = MinecraftServer.getGlobalEventHandler()

        fun subscribe(clazz: Class<out Event>, action: EventAction) {
            if (listenerCache.containsKey(clazz)) {
                val currentEventActions: ArrayList<EventAction> = listenerCache[clazz]!!
                currentEventActions.add(action)
                return
            }

            listenerCache[clazz] = arrayListOf(action)
            globalEventHandler.addListener(clazz) { event ->
                for (action in listenerCache[event.javaClass]!!) {
                    action.execute(event)
                }
            }
        }
    }
}