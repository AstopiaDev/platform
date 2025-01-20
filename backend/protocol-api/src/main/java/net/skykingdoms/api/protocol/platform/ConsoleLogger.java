package net.skykingdoms.api.protocol.platform;

import net.skykingdoms.impl.Impl;
import net.skykingdoms.impl.ImplPriority;

import java.util.logging.*;

@Impl(value = Platform.class, priority = ImplPriority.HIGHEST)
public final class ConsoleLogger extends AbstractLogger {
    public ConsoleLogger() {
        super("net.skykingdoms.api");

        SimpleFormatter fmt = new SimpleFormatter();
        StreamHandler sh = new StreamHandler(System.out, fmt);
        sh.setLevel(Level.ALL);

        this.addHandler(sh);
    }
}
