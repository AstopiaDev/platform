package net.skykingdoms.api.shared.ui.sui

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.scoreboard.Sidebar
import net.skykingdoms.api.shared.GameServer
import net.skykingdoms.api.shared.event.PlayerJoinEvent
import net.skykingdoms.api.shared.ui.UserInterface

abstract class SideUi(loader: Player, title: Component) : Sidebar(title), UserInterface {
    companion object {
        private val sideUis: HashMap<Player, SideUi> = hashMapOf()

        fun init(gameServer: GameServer) {
            val sideBarNode = EventNode.type("sidebar", EventFilter.PLAYER)
                .addListener(PlayerJoinEvent::class.java) { event ->
                    gameServer.defaultSideUi(event.player)
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