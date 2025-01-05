package net.skykingdoms.api.protocol.platform;

import net.skykingdoms.impl.Impls;

import java.util.logging.Logger;

public abstract class Platform {
    public static AbstractLogger logger;
    public static IClientChannel channel;
    private static final Logger loggerImp = Impls.get(Logger.class);

    public static void init(AbstractLogger logger, IClientChannel channel) {
        Platform.logger = logger;
        Platform.channel = channel;
        if (loggerImp != null) {
            Platform.logger.setParent(loggerImp);
        }
        Platform.logger.addHandler(new PacketLoggerHandler());
    }
}
