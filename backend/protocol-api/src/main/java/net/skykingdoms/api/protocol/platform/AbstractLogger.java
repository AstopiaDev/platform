package net.skykingdoms.api.protocol.platform;

import lombok.Setter;
import net.skykingdoms.api.protocol.util.TextFormat;

import java.lang.management.ManagementFactory;
import java.util.MissingResourceException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

@Setter
public class AbstractLogger extends Logger {
    private boolean debug;

    /**
     * Protected method to construct a logger for a named subsystem.
     * <p>
     * The logger will be initially configured with a null Level
     * and with useParentHandlers set to true.
     *
     * @param name               A name for the logger.  This should
     *                           be a dot-separated name and should normally
     *                           be based on the package name or class name
     *                           of the subsystem, such as java.net
     *                           or javax.swing.  It may be null for anonymous Loggers.
     * @param resourceBundleName name of ResourceBundle to be used for localizing
     *                           messages for this logger.  May be null if none
     *                           of the messages require localization.
     * @throws MissingResourceException if the resourceBundleName is non-null and
     *                                  no corresponding resource can be found.
     */
    protected AbstractLogger(String name, String resourceBundleName) {
        super(name, null);

        boolean debug = false;

        for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            if (arg.contains("jdwp=")) {
                debug = true;
                break;
            }
        }

        this.debug = debug;
    }

    /**
     * @param name A name for the logger.  This should
     *             be a dot-separated name and should normally
     *             be based on the package name or class name
     *             of the subsystem, such as java.net
     */
    public AbstractLogger(String name) {
        this(name, null);
    }

    public void warn(String message) {
        this.warning(message);
    }

    public void warn(Supplier<String> message) {
        this.warning(message);
    }

    public void debug(String message) {
        if (debug) {
            this.finer(message);
        }
    }

    public void debug(Supplier<String> message) {
        if (debug) {
            this.finer(message);
        }
    }

    public void error(String message) {
        this.severe(message);
    }

    public void stacktrace(Throwable t, String message) {
        this.log(Level.SEVERE, message, t);
    }

    public void debug(String message, Object... args) {
        debug(TextFormat.formatText(message, args));
    }

    public void info(String message, Object... args) {
        info(TextFormat.formatText(message, args));
    }

    public void warn(String message, Object... args) {
        warn(TextFormat.formatText(message, args));
    }


    public void error(String message, Object... args) {
        error(TextFormat.formatText(message, args));
    }

    public void stacktrace(Throwable t, String message, Object... args) {
        stacktrace(t, TextFormat.formatText(message, args));
    }
}
