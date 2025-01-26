package net.astopia.backend.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import net.astopia.api.backend.util.config.Configuration;
import net.astopia.api.backend.util.config.ConfigurationProvider;
import net.astopia.api.backend.util.config.YamlConfiguration;
import net.astopia.api.protocol.platform.Platform;
import net.astopia.backend.base.data.state.NetworkState;
import net.astopia.backend.base.language.Language;
import net.astopia.backend.protocol.type.reconnect.impl.FluentProxyDataImpl;
import net.astopia.backend.velocity.boot.VelocityConnectorBoot;
import net.astopia.backend.velocity.connection.Connection;
import net.astopia.backend.velocity.connection.VelocityLocalClientConnection;
import net.astopia.backend.velocity.listener.KickListener;
import net.astopia.backend.velocity.listener.PingListener;
import net.astopia.backend.velocity.listener.PlayerListener;
import net.astopia.backend.velocity.listener.TabCompleteListener;
import net.astopia.backend.velocity.util.MinecraftAuthenticator;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(fluent = true)
@Getter
public enum VelocityConnector {
    INSTANCE;

    @Setter NetworkState networkState = NetworkState.OPEN;
    VelocityLocalClientConnection connection;
    @Setter FluentProxyDataImpl proxyData;
    @Setter boolean reconnecting = false;
    boolean initialized = false;
    Configuration configuration;
    MinecraftAuthenticator authenticator = new MinecraftAuthenticator();
    VelocityConnectorBoot boot;
    ProxyServer server;

    public void construct(VelocityConnectorBoot boot, ProxyServer proxyServer) {
        this.boot = boot;
        this.server = proxyServer;

        try {
            Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public void init() {
        initialized = true;
        this.reloadConfiguration();

        this.connection = new VelocityLocalClientConnection(
                new InetSocketAddress(configuration.getString("coordinatorHost"), configuration.getInt("coordinatorPort")),
                2
        );

        this.proxyData = new FluentProxyDataImpl("N/A", "N/A",
                System.getenv("EXTERNAL_ADDR"),
                Integer.parseInt(System.getenv("PROXY_HOST").split(":")[1])
        );

        Platform.logger.info("External address: {}", this.proxyData.getHost());
        Language.reloadLanguages();


        connection.connectSynchronized();
        Connection.setConnection(connection);

        server.getEventManager().register(boot, new PlayerListener());
        server.getEventManager().register(boot, new PingListener());
        server.getEventManager().register(boot, new TabCompleteListener());
        server.getEventManager().register(boot, new KickListener());

/*
        BungeeSecurityManager.INSTANCE.postBootstrap();
*/
    }

    public void reloadConfiguration() {
        val provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
        val configFile = new File("proxy-config.yml");

        try {
            if (!configFile.exists()) {
                Configuration defaultConfiguration = new Configuration();
                defaultConfiguration.set("coordinatorHost", "");
                defaultConfiguration.set("coordinatorPort", -1);

                //noinspection ResultOfMethodCallIgnored
                configFile.createNewFile();

                provider.save(defaultConfiguration, configFile);
            }

            configuration = provider.load(configFile);
        } catch (IOException e) {
            Platform.logger.stacktrace(e, "Can't load configuration file");
        }
    }

    public int getGlobalOnline() {
        return server.getPlayerCount();
    }
}
