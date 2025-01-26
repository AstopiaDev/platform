package net.astopia.backend.bukkit;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.val;
import net.astopia.api.backend.util.config.Configuration;
import net.astopia.api.backend.util.config.ConfigurationProvider;
import net.astopia.api.backend.util.config.YamlConfiguration;
import net.astopia.api.protocol.platform.Platform;
import net.astopia.api.protocol.server.GameType;
import net.astopia.api.protocol.server.ServerScaleType;
import net.astopia.api.protocol.type.GameMode;
import net.astopia.api.protocol.util.TextFormat;
import net.astopia.backend.base.language.Language;
import net.astopia.backend.bukkit.boot.BukkitConnectorBoot;
import net.astopia.backend.bukkit.connection.BukkitLocalClientConnection;
import net.astopia.backend.bukkit.connection.Connection;
import net.astopia.backend.bukkit.security.BukkitSecurityManager;
import net.astopia.backend.protocol.exception.ConnectException;
import net.astopia.backend.protocol.type.reconnect.impl.FluentBukkitDataImpl;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(fluent = true)
@Getter
public enum BukkitConnector {
    INSTANCE;

    BukkitLocalClientConnection connection;
    FluentBukkitDataImpl fluentBukkitData;
    ServerScaleType serverScaleType;
    @Setter boolean reconnecting = false;
    Configuration configuration;
    BukkitConnectorBoot plugin;
    SocketAddress address;
    UUID transactionId;
    GameType gameType;
    GameMode gameMode;
    int testFlowId;
    String secret;

    public void construct(BukkitConnectorBoot plugin) {
        this.plugin = plugin;

        plugin.saveDefaultConfig();
        this.loadConfiguration();

        // props
        this.gameMode = GameMode.valueOf(System.getProperty("gameMode").toUpperCase(), null);
        this.gameType = GameType.valueOf(System.getProperty("gameType").toUpperCase());
        this.serverScaleType = ServerScaleType.valueOf(System.getProperty("serverScale").toUpperCase());

        // env vars
        this.secret = configuration == null ? System.getenv("CORD_SECRET") : configuration.getString("CORD_SECRET");
        this.transactionId = configuration == null ? UUID.fromString(System.getenv("CORD_TID")) : UUID.fromString(configuration.getString("CORD_TID"));
        this.testFlowId = Integer.parseInt(System.getProperty("testFlowId", "-1"));

        Platform.logger.info("[TestFlow] -> {}", testFlowId);

        this.address = new InetSocketAddress(
                plugin.getConfig().getString("megamind-ip"),
                plugin.getConfig().getInt("megamind-port")
        );
    }

    public void loadConfiguration() {
        val provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
        val configFile = new File("config.yml");

        if (configFile.exists()) {
            try {
                configuration = provider.load(configFile);
             //   secret = configuration.getString("secret");
            } catch (IOException e) {
                Platform.logger.stacktrace(e, "Can't load configuration file");
            }
        }
    }

    public void init() {
        this.connection = new BukkitLocalClientConnection(address, 2);

        // we init this shit for handshake cocked stuff
        Platform.logger.info("S: {} | G: {} | EX: {} | P: {}", serverScaleType, gameType, System.getProperty("externalAddr"), bukkitAddress().getPort());
        this.fluentBukkitData = new FluentBukkitDataImpl(gameMode != null ? gameMode.name().toLowerCase() : "N/A", "N/A", serverScaleType, gameType,
                System.getProperty("externalAddr"), bukkitAddress().getPort());

        plugin.getServer().getScheduler().runTask(plugin, () -> {
            try {
                this.connection.connectSynchronized();
                Platform.logger.info("Connection synchronized!");
            } catch (ConnectException e) {
                e.printStackTrace();
            }
        });

        BukkitSecurityManager.INSTANCE.postBootstrap();

        Connection.setConnection(connection);
    }

    public void load() {
        Language.reloadLanguages();
    }

    public void shutdown() {
        for (var player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer(TextFormat.formatText("Server {} shut down at {}",
                    fluentBukkitData.getServerName(), fluentBukkitData.getNodeName()));
        }
    }

    public @NotNull InetSocketAddress bukkitAddress() {
        val env = configuration == null ? System.getenv("BUKKIT_HOST").split(":") : configuration.getString("BUKKIT_HOST").split(":"); // todo: refactor. shitcode
        return InetSocketAddress.createUnresolved(env[0], Integer.parseInt(env[1]));
    }
}
