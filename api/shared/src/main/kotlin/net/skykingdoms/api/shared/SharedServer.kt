package net.skykingdoms.api.shared

import net.minestom.server.MinecraftServer
import net.skykingdoms.api.protocol.platform.ConsoleLogger
import net.skykingdoms.api.protocol.platform.Platform
import net.skykingdoms.api.shared.dummy.Dummy
import net.skykingdoms.api.shared.skin.PlayerSkinHandler
import net.skykingdoms.api.shared.ui.gui.Gui
import net.skykingdoms.api.shared.ui.sui.SideUi
import net.skykingdoms.api.shared.ui.tui.TabUi
import net.skykingdoms.api.shared.user.ServerUser
import net.skykingdoms.api.shared.user.ServerUserProvider
import net.skykingdoms.backend.base.language.Language
import net.skykingdoms.backend.minestom.MinestomConnector
import net.skykingdoms.backend.minestom.event.ConnectionEstablishedEvent

abstract class SharedServer : Server {
    val minecraftServer = MinecraftServer.init()
    val instanceManager = MinecraftServer.getInstanceManager()
    val connectionManager = MinecraftServer.getConnectionManager()
    val commandManager = MinecraftServer.getCommandManager()
    val schedulerManager = MinecraftServer.getSchedulerManager()
    val globalEventHandler = MinecraftServer.getGlobalEventHandler()
    val dimensionTypeManager = MinecraftServer.getDimensionTypeRegistry()

    fun start() {
        Language.reloadLanguages()
        Dummy.init()

        TabUi.init(globalEventHandler)
        Gui.init(globalEventHandler)
        PlayerSkinHandler.init(globalEventHandler)

        MinestomConnector.init()

        connectionManager.setPlayerProvider(ServerUserProvider())
        minecraftServer.start("0.0.0.0", 25565) //MinestomConnector.instanceData.port)
        schedulerManager.buildShutdownTask {
            stop()
        }
        globalEventHandler
            .addListener(ConnectionEstablishedEvent::class.java) {
                Platform.logger.info("[connection] Active")
                active()
            }

        onStart()
    }

    private fun stop() {
        //MinestomConnector.shutdown()
        onStop()
    }

    private fun active() {
        onActive()
    }

    open fun defaultSideUi(loader: ServerUser): SideUi? {
        return null
    }
}