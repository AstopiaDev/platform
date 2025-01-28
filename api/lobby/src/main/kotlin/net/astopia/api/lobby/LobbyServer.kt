package net.astopia.api.lobby

import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.instance.IChunkLoader
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.registry.DynamicRegistry
import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.DimensionType
import net.astopia.api.backend.util.config.Configuration
import net.astopia.api.backend.util.config.ConfigurationProvider
import net.astopia.api.lobby.hotbar.HotBar
import net.astopia.api.protocol.platform.Platform
import net.astopia.api.shared.SharedServer
import net.astopia.api.shared.chat.ServerChat
import net.astopia.api.shared.util.LocationUtil
import net.astopia.api.shared.world.loader.slime.SlimeFormatLoader
import net.astopia.api.shared.world.source.FileWorldSource
import net.astopia.api.shared.world.source.WorldSource
import net.astopia.backend.minestom.connection.Connection
import net.astopia.backend.protocol.packet.play.LobbyBoot
import net.minestom.server.entity.Player
import java.io.File

abstract class LobbyServer : SharedServer() {
    var name: String? = null;
    lateinit var config: Configuration;
    lateinit var spawnPoint: Pos;

    override fun onStart() {
        val instanceContainer = createInstanceContainer()

        config = ConfigurationProvider.YAML.load(File("config.yml"))
        spawnPoint = LocationUtil.strToPos(config.getString("spawn_point"))

        globalEventHandler.addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
            event.spawningInstance = instanceContainer
            event.isHardcore = true

            val player = event.player
            player.respawnPoint = spawnPoint
            player.gameMode = GameMode.ADVENTURE
        }

        LobbyHandler.init(this)
        HotBar.init(globalEventHandler)
        ServerChat.init(globalEventHandler)
    }

    override fun onStop() {
    }

    override fun onActive() {
        Connection.awaitPacket(LobbyBoot.Request(100, name))?.thenAccept { response ->
            name = response.lobbyName
            Platform.logger.info("[Lobby] Successfully deployed this lobby instance. Name: {}", name)
        }
    }

    private fun createInstanceContainer(): InstanceContainer {
        val fullBrightDimension = DimensionType.builder()
            .ambientLight(2.0f)
            .build();
        dimensionTypeManager.register(NamespaceID.from("skykingdoms:full_bright"), fullBrightDimension)

        val instanceContainer = instanceManager.createInstanceContainer(
            dimensionTypeManager.getKey(fullBrightDimension) as DynamicRegistry.Key<DimensionType>
        )

        val file = File("world.slime")
        val worldSource: WorldSource = FileWorldSource(file)
        val slimeLoader: IChunkLoader = SlimeFormatLoader(instanceContainer, worldSource)

        instanceContainer.chunkLoader = slimeLoader

        return instanceContainer
    }
}
