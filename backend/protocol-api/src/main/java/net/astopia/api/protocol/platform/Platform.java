package net.astopia.api.protocol.platform;

import java.util.logging.Logger;

public abstract class Platform {
    public static AbstractLogger logger;
    public static IClientChannel channel;

    public static void init(AbstractLogger logger, IClientChannel channel, Logger loggerImp) {
        Platform.logger = logger;
        Platform.channel = channel;

        Platform.logger.addHandler(new PacketLoggerHandler());

        if (loggerImp != null) {
            Platform.logger.setParent(loggerImp);
        }
    }

    public static void init(AbstractLogger logger, IClientChannel channel) {
        init(logger, channel, null);
    }

    public static void init(AbstractLogger logger) {
        Platform.logger = logger;
    }
}
