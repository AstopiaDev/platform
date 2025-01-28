package net.astopia.api.protocol.platform;

import net.astopia.impl.Impl;
import net.astopia.impl.ImplPriority;

import java.util.logging.*;

@Impl(value = Platform.class, priority = ImplPriority.HIGHEST)
public final class ConsoleLogger extends AbstractLogger {
    public ConsoleLogger() {
        super("net.astopia.api");

        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");

        ConsoleFormatter fmt = new ConsoleFormatter();
        StreamHandler sh = new StreamHandler(System.out, fmt);
        sh.setLevel(Level.ALL);

        this.addHandler(sh);
    }
}
