import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.instance.IChunkLoader
import net.minestom.server.instance.anvil.AnvilLoader
import net.skykingdoms.api.backend.util.config.Configuration
import net.skykingdoms.api.backend.util.config.ConfigurationProvider
import net.skykingdoms.api.protocol.logging.Logging
import net.skykingdoms.api.shared.GameServer
import net.skykingdoms.api.shared.chat.ServerChat
import net.skykingdoms.backend.minestom.connection.Connection
import net.skykingdoms.backend.protocol.packet.play.LobbyBoot
import net.skykingdoms.api.shared.util.LocationUtil
import net.skykingdoms.api.shared.world.loader.minestom.MinestomSlimeFormatLoader
import net.skykingdoms.api.shared.world.loader.slime.SlimeFormatLoader
import net.skykingdoms.api.shared.world.source.FileWorldSource
import net.skykingdoms.api.shared.world.source.WorldSource
import java.io.File

abstract class LobbyServer : GameServer() {
    var name: String? = null;
    lateinit var config: Configuration;
    lateinit var spawnPoint: Pos;

    override fun onStart() {
        val instanceContainer = instanceManager.createInstanceContainer()
        val file = File("world.slime")
        val worldSource: WorldSource = FileWorldSource(file)
        val slimeLoader: IChunkLoader = SlimeFormatLoader(instanceContainer, worldSource)

        instanceContainer.chunkLoader = slimeLoader
        /*instanceContainer.chunkLoader = AnvilLoader("world")*/

        val loader: AnvilLoader


        config = ConfigurationProvider.YAML.load(File("config.yml"))
        spawnPoint = LocationUtil.strToPos(config.getString("spawn_point"))

        /*SlimeFormatSerializer().createFile(instanceContainer, file)*/

        globalEventHandler.addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
            event.spawningInstance = instanceContainer
            event.player.respawnPoint = spawnPoint
        }

        ServerChat.init()
    }

    override fun onStop() {
    }

    override fun onActive() {
        Connection.awaitPacket(LobbyBoot.Request(100, name))?.thenAccept { response ->
            name = response.lobbyName
            Logging.IMP.info("[Lobby] Successfully deployed this lobby instance. Name: {}", name)
        }
    }
}
