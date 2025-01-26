package net.astopia.backend.tcpshield.listener;

import lombok.NonNull;
import lombok.val;
import net.astopia.api.megamind.event.EventHandler;
import net.astopia.api.megamind.event.Listener;
import net.astopia.api.protocol.platform.Platform;
import net.astopia.backend.megamind.MegamindApp;
import net.astopia.backend.megamind.event.network.ProxyDeployEvent;
import net.astopia.backend.megamind.event.network.ProxyRemoveEvent;
import net.astopia.backend.tcpshield.TcpShieldModule;
import net.astopia.backend.tcpshield.manager.TcpShieldManager;

public final class ProxyDeploymentListener implements Listener {

    TcpShieldModule instance;

    public ProxyDeploymentListener(final @NonNull TcpShieldModule instance) {
        MegamindApp.INSTANCE.pluginManager().registerListener(this);
        this.instance = instance;
    }

    @EventHandler
    public void onProxyDeploy(final ProxyDeployEvent event) {
        val proxy = event.getProxyServer();
        TcpShieldManager.INSTANCE.createBackend(
                proxy.getHost().getHostString(),
                proxy.getHost().getPort());
        Platform.logger.info("[TcpShield] Deployed proxy at {}{} {}", proxy.getHost().getHostString() + ":" + proxy.getHost().getPort(),
                event.isReconnect() ? " (reconnect)" : "");
    }

    @EventHandler
    public void onProxyRemoval(final ProxyRemoveEvent event) {
        val proxy = event.getProxyServer();
        val host = proxy.getHost();
        TcpShieldManager.INSTANCE.removeBackend(host.getHostString(), host.getPort());
        Platform.logger.info("[TcpShield] Removed proxy at {}", proxy.getHost().getHostString() + ":" + proxy.getHost().getPort());
    }

}
