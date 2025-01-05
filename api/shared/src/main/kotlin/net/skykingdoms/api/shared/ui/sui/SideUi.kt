package net.skykingdoms.api.shared.ui.sui

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerSkinInitEvent
import net.minestom.server.scoreboard.Sidebar
import net.skykingdoms.api.shared.GameServer
import net.skykingdoms.api.shared.ui.UserInterface
import net.skykingdoms.api.shared.user.ServerUser
import java.util.concurrent.ConcurrentHashMap

abstract class SideUi(val loader: ServerUser, title: Component) : Sidebar(title), UserInterface {
    companion object {
        private val sideUis: ConcurrentHashMap<ServerUser, SideUi> = ConcurrentHashMap()

        fun init(gameServer: GameServer) {
            val sideBarNode = EventNode.type("sidebar", EventFilter.PLAYER)
                .addListener(PlayerSkinInitEvent::class.java) { event ->
                    gameServer.defaultSideUi(event.player as ServerUser)
                }
                .addListener(PlayerDisconnectEvent::class.java) { event ->
                    remove(event.player)
                }

            gameServer.globalEventHandler.addChild(sideBarNode)
        }

        fun remove(player: Player) {
            sideUis.remove(player)?.removeViewer(player)
        }
    }

    init {
        sideUis.put(loader, this)
        this.addViewer(loader)
        draw()
    }

    fun line(content: Component, line: Int) {
        val id: String = "line-$line"
        val line: ScoreboardLine = ScoreboardLine(id, content, line)

        if (this.getLine(id) == null) {
            createLine(line)
            return;
        }

        updateLineContent(id, content)
    }
}