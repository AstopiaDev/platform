package net.skykingdoms.api.lobby.hotbar

import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.skykingdoms.api.lobby.hotbar.impl.LobbyHotBar
import net.skykingdoms.api.shared.item.Item
import net.skykingdoms.api.shared.ui.gui.ClickAction
import net.skykingdoms.api.shared.user.ServerUser
import java.util.concurrent.ConcurrentHashMap

abstract class HotBar(private val items: Array<Item>, private val invClickActions: Array<ClickAction>) {
    companion object {
        private val playerHotBars: ConcurrentHashMap<ServerUser, HotBar> = ConcurrentHashMap()
        private val hotBars: ConcurrentHashMap<Byte, HotBar> = ConcurrentHashMap()
        private var currentId: Byte = 0x0

        init {
            register(LobbyHotBar())
        }

        fun register(hotBar: HotBar): Byte {
            if (currentId == Byte.MAX_VALUE) {
                throw IllegalStateException("Maximum number of HotBars exceeded (Byte overflow)")
            }

            hotBars[currentId] = hotBar
            val assignedKey = currentId
            currentId = (currentId + 1).toByte()
            return assignedKey
        }

        fun register(byte: Byte, hotBar: HotBar) {
            hotBars[byte] = hotBar
            currentId = (currentId + 1).toByte()
        }

        fun get(byte: Byte): HotBar? {
            return hotBars[byte]
        }

        fun init(globalEventHandler: GlobalEventHandler) {
            globalEventHandler
                .addListener(PlayerDisconnectEvent::class.java) { event ->
                    val player = event.player as ServerUser
                    playerHotBars.remove(player)
                }
                .addListener(InventoryPreClickEvent::class.java) { event ->
                    val player = event.player as ServerUser

                    if (!playerHotBars.containsKey(player)) {
                        return@addListener
                    }

                    playerHotBars[player]?.invClickActions?.get(event.slot)?.execute(event)
                }
        }
    }

    fun load(loader: ServerUser) {
        loader.inventory.clear()
        for ((index, item) in items.withIndex()) {
            loader.inventory.setItemStack(index, item.itemStack)
        }
        playerHotBars[loader] = this
    }
}