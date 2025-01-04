package net.skykingdoms.api.shared

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.skykingdoms.api.protocol.logging.Logging
import net.skykingdoms.api.shared.ui.gui.Gui
import net.skykingdoms.api.shared.ui.sui.SideUi
import net.skykingdoms.backend.minestom.MinestomConnector
import net.skykingdoms.backend.minestom.event.ConnectionEstablishedEvent

abstract class GameServer : Server {
    val minecraftServer = MinecraftServer.init()
    val instanceManager = MinecraftServer.getInstanceManager()
    val connectionManager = MinecraftServer.getConnectionManager()
    val commandManager = MinecraftServer.getCommandManager()
    val schedulerManager = MinecraftServer.getSchedulerManager()
    val globalEventHandler = MinecraftServer.getGlobalEventHandler()

    fun start() {
        //MinestomConnector.init()
        Gui.init(globalEventHandler)

        minecraftServer.start("0.0.0.0", 25565) //MinestomConnector.instanceData.port)
        schedulerManager.buildShutdownTask {
            stop()
        }
        globalEventHandler
            .addListener(ConnectionEstablishedEvent::class.java) {
                Logging.IMP.info("[connection] Active")
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

    open fun defaultSideUi(loader: Player): SideUi? {
        return null
    }
}