package net.astopia.api.shared

import net.minestom.server.MinecraftServer
import net.astopia.api.protocol.platform.ConsoleLogger
import net.astopia.api.protocol.platform.Platform
import net.astopia.api.shared.dummy.Dummy
import net.astopia.api.shared.skin.PlayerSkinHandler
import net.astopia.api.shared.ui.gui.Gui
import net.astopia.api.shared.ui.sui.SideUi
import net.astopia.api.shared.ui.tui.TabUi
import net.astopia.api.shared.user.ServerUser
import net.astopia.api.shared.user.ServerUserProvider
import net.astopia.backend.base.language.Language
import net.astopia.backend.minestom.MinestomConnector
import net.astopia.backend.minestom.event.ConnectionEstablishedEvent

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

        Platform.init(ConsoleLogger())
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