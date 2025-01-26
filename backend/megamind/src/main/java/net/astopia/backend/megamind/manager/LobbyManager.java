package net.astopia.backend.megamind.manager;


import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import net.astopia.api.backend.util.collection.CollectionUtil;
import net.astopia.api.protocol.platform.Platform;
import net.astopia.api.protocol.server.GameType;
import net.astopia.backend.base.dependency.Dependency;
import net.astopia.backend.megamind.MegamindApp;
import net.astopia.backend.megamind.data.gamer.Gamer;
import net.astopia.backend.megamind.lookup.LookupManager;
import net.astopia.backend.megamind.manager.deployment.DeploymentResponse;
import net.astopia.backend.megamind.server.impl.BukkitServer;
import net.astopia.backend.megamind.server.impl.LobbyServer;
import net.astopia.backend.protocol.packet.play.S06RegisterServers;
import net.astopia.backend.protocol.packet.play.ServerDeploy;
import net.astopia.backend.protocol.server.deploy.impl.BukkitDeployRequest;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Basic manager that responds to deploying, registering and updating lobby
 * instances.
 *
 *
 */
@Getter
public final class LobbyManager {

    // ------------------------------------------------------------
    @Dependency
    public static final LobbyManager INSTANCE = new LobbyManager();
    // ------------------------------------------------------------

    // Set of registered lobby servers
    Collection<LobbyServer> registeredLobbyServers = CollectionUtil.createConcurrentSet();

    /**
     * Invoked once in coordinator bootstrap task
     */
    public void postBootstrap() {
        Platform.logger.info("Scheduled lobby cluster deployment process");
        Platform.logger.info("If all required lobbyservers come online in next 1 minute");
        Platform.logger.info("The process aborts it's scheduling");
        Platform.logger.info("");

        // min. amount of lobbyservers per each game type
        // will be loaded from config in a map <GameType> - <Min.Amount>
        val HARDCODED_RETARDED_NUMBER = 1;

        MegamindApp.INSTANCE.scheduler().schedule(() -> {
            for (val gametype : GameType.VALUES) {
                if (!gametype.isHasLobby()) continue;
                val amount = registeredLobbyServers.stream()
                        .filter(_zvo -> _zvo.getParentServer().getGameType() == gametype)
                        .count();
                if (amount < HARDCODED_RETARDED_NUMBER) {
                    Platform.logger.info("[LobbyManager] There are not enough lobbies loaded up for {} (current: {})",
                            gametype.name(), amount);

                    DeploymentManager.INSTANCE.sendDeploymentRequest(new BukkitDeployRequest(gametype.bakeLobbyData()),
                            ServerDeploy.DeploymentTarget.BUKKIT, false, null, -1,
                            (result) -> {
                                if (result == DeploymentResponse.DENIED_BY_NODE
                                        || result == DeploymentResponse.DEPLOYMENT_TIMEOUT
                                        || result == DeploymentResponse.TRANSACTION_FAILED) {
                                    Platform.logger.error("Transaction failed! (" + result.name() + ") / (#" + result.transactionId() + ")");
                                } else {
                                    Platform.logger.info("Queued deployment of lobby for {}!", gametype.getHumanName());
                                }
                            });
                }
            }
        }, 60, TimeUnit.SECONDS);
    }

    /**
     * Removes a lobby server and routes all gamers to fallback
     */
    public void removeLobbyServer(final @NonNull LobbyServer lobbyServer) {
        if (!registeredLobbyServers.contains(lobbyServer)) {
            return; // dont do shit cuz its not even registered
        }

        Platform.logger.info("[LobbyManager] Removed lobby {} with {} players online",
                lobbyServer.getName(), lobbyServer.getPlayerIds().length);

        for (val playerId : lobbyServer.getPlayerIds()) {
            val gamer = Gamer.getGamer(playerId);
            // todo: route to fallback
        }

        registeredLobbyServers.remove(lobbyServer);

        // handling removing in lookups
        LookupManager.INSTANCE.handleServerRemove(lobbyServer);
    }

    public LobbyServer getLobbyServer(final @NonNull String lobbyName) {
        return registeredLobbyServers.stream()
                .filter(s -> s.getName().equalsIgnoreCase(lobbyName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Basically adds a new lobby server instance to registry
     * @param server    instance
     * @return          self-instance
     */
    public LobbyServer addLobbyServer(final @NonNull LobbyServer server) {
        if (registeredLobbyServers.contains(server)) {
            throw new IllegalStateException("Lobby is already registered");
        }

        ProxyManager.INSTANCE.sendToProxies(new S06RegisterServers(
                server.getName(),
                server.getParentServer().getHost().getHostString(),
                server.getParentServer().getHost().getPort(),
                server.getParentServer().getScaleType(),
                server.getParentServer().getGameType()));

        registeredLobbyServers.add(server);

        LookupManager.INSTANCE.handleServerCreate(server);

        return server;
    }

    public LobbyServer getLinkedLobbyServer(final @NonNull BukkitServer server) {
        return registeredLobbyServers.stream()
                .filter(lobbyServer -> lobbyServer.getParentServer().equals(server))
                .findFirst()
                .orElse(null);
    }

    /**
     * Basically prepares deployment process of lobby-server
     */
    public void deployLobbyServer(final @NonNull GameType gameType) {
        if (!gameType.isHasLobby()) {
            throw new UnsupportedOperationException("Cannot boot lobby for this GameType");
        }

        if (registeredLobbyServers.size() == 0) {
            Platform.logger.info("[LobbyManager] Tried deploying a lobby, but no BukkitServer is up yet ... ");
            Platform.logger.info("[LobbyManager] Deploying required amount of instances ({})");
        }

    }
}