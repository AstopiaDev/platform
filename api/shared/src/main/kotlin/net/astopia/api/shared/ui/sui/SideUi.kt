package net.astopia.api.shared.ui.sui

import net.astopia.api.backend.adventure.Adventure.MM
import net.astopia.api.protocol.platform.Platform
import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerSkinInitEvent
import net.minestom.server.scoreboard.Sidebar
import net.astopia.api.shared.SharedServer
import net.astopia.api.shared.ui.UserInterface
import net.astopia.api.shared.user.ServerUser
import java.util.concurrent.ConcurrentHashMap

abstract class SideUi(val loader: ServerUser, title: Component) : Sidebar(title), UserInterface {
    companion object {
        private val sideUis: ConcurrentHashMap<ServerUser, SideUi> = ConcurrentHashMap()

        fun init(sharedServer: SharedServer) {
            val sideBarNode = EventNode.type("sidebar", EventFilter.PLAYER)
                .addListener(PlayerSkinInitEvent::class.java) { event ->
                    sharedServer.defaultSideUi(event.player as ServerUser)
                }
                .addListener(PlayerDisconnectEvent::class.java) { event ->
                    remove(event.player)
                }

            sharedServer.globalEventHandler.addChild(sideBarNode)
        }

        fun remove(player: Player) {
            sideUis.remove(player)?.removeViewer(player)
        }
    }

    init {
        sideUis.put(loader, this)
        this.addViewer(loader)
        Platform.logger.info("Added ${loader.name}")
        draw()
    }

    fun line(content: Component, line: Int) {
        val id: String = "line-$line"
        val line: ScoreboardLine = ScoreboardLine(id, content, line)

        if (this.getLine(id) == null) {
            Platform.logger.info("Setting $line as ${MM.serialize(content)}")
            createLine(line)
            return;
        }

        updateLineContent(id, content)
    }
}