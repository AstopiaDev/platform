package net.astopia.backend.node.boot;

import net.astopia.api.protocol.platform.Platform;
import net.astopia.backend.node.NodeApp;
import org.jetbrains.annotations.NotNull;

public class NodeBoot {
    public static void main(final String @NotNull [] args) {
        Platform.logger.info("[EntryPoint] Bootstrapping node driver ...");
        try {
            NodeApp.INSTANCE.init();
        } catch (Throwable throwable) {
            Platform.logger.stacktrace(throwable, "Internal error occurred");
        }
    }
}
