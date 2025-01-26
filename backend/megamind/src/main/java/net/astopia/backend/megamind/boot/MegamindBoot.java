package net.astopia.backend.megamind.boot;

import net.astopia.api.protocol.platform.ConsoleLogger;
import net.astopia.api.protocol.platform.Platform;
import net.astopia.backend.megamind.MegamindApp;

public class MegamindBoot {
    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("io.netty.selectorAutoRebuildThreshold", "0");

        try {
            Platform.init(new ConsoleLogger());
            MegamindApp.INSTANCE.init();
        } catch (Throwable t) {
            Platform.logger.stacktrace(t, "Internal error occurred");
            System.exit(-1);
        }
    }
}
