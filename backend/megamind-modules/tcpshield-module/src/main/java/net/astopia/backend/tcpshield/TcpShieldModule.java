package net.astopia.backend.tcpshield;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.val;
import net.astopia.api.protocol.platform.Platform;
import net.astopia.backend.megamind.MegamindApp;
import net.astopia.backend.megamind.plugin.Plugin;
import net.astopia.backend.tcpshield.command.TcpShieldBackendsCommand;
import net.astopia.backend.tcpshield.listener.ProxyDeploymentListener;
import net.astopia.backend.tcpshield.manager.TcpShieldManager;

@FieldDefaults(level = AccessLevel.PRIVATE)
@SuppressWarnings("DeprecatedIsStillUsed")
@Getter
public final class TcpShieldModule extends Plugin {

    // ------------------------------------------------------
    @Deprecated
    public static TcpShieldModule INSTANCE;
    // ------------------------------------------------------

    /**
     * NeoProtect API key
     */
    String api;

    @Override
    public void onEnable() {
        INSTANCE = this;

        saveDefaultConfig();
        loadConfig();

        this.api = getConfig().getString("api-token");
        //getConfig().getString("server-id");

        TcpShieldManager.INSTANCE.postBootstrap(
                this.api,
                getConfig().getInt("server-id"),
                getConfig().getString("backend-grup-name"),
                getConfig().getInt("backend-group-id")
        );

        MegamindApp.INSTANCE.commandManager().addCommand(TcpShieldBackendsCommand.class);

        new ProxyDeploymentListener(this);

        val backends = TcpShieldManager.INSTANCE.getLoadedBackends();
        Platform.logger.info("In total {} backends loaded (local){}", backends.size(), !backends.isEmpty() ? ":" : "");
/*        for (val backend : backends) {
            Logging.IMP.info("{} -> {}:{}", backend.getId(), backend.getIp(), backend.getPort());
        }*/
    }
}
