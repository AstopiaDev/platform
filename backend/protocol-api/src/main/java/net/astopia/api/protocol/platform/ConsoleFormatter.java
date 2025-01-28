package net.astopia.api.protocol.platform;

import net.astopia.api.protocol.util.TextFormat;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ConsoleFormatter extends Formatter {
    // ANSI escape codes
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_GRAY = "\u001B[90m";

    private static final String ANSI_BOLD_BLACK = "\u001B[1;30m";
    private static final String ANSI_BOLD_RED = "\u001B[1;31m";
    private static final String ANSI_BOLD_GREEN = "\u001B[1;32m";
    private static final String ANSI_BOLD_YELLOW = "\u001B[1;33m";
    private static final String ANSI_BOLD_BLUE = "\u001B[1;34m";
    private static final String ANSI_BOLD_PURPLE = "\u001B[1;35m";
    private static final String ANSI_BOLD_CYAN = "\u001B[1;36m";
    private static final String ANSI_BOLD_WHITE = "\u001B[1;37m";
    private static final String ANSI_BOLD_GRAY = "\u001B[1;90m";

    private static final SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd.MM HH:mm:ss");

    @Override
    public String format(LogRecord record) {
        return String.format("%s%s %s: %s%n",
                ANSI_GRAY,
                dateFormat.format(Instant.ofEpochMilli(record.getMillis())),
                getColoredLevel(record.getLevel()),
                TextFormat.formatMcToAnsi(record.getMessage()));
    }

    private String getColoredLevel(Level level) {
        String levelName = level.getName();

        if (levelName.equals("FINE") ||
                levelName.equals("FINER") ||
                levelName.equals("FINEST")) {
            levelName = "DEBUG";
        }

        String color = switch (levelName) {
            case "SEVERE" -> ANSI_BOLD_RED;
            case "WARNING" -> ANSI_BOLD_YELLOW;
            case "INFO" -> ANSI_BOLD_GREEN;
            case "CONFIG" -> ANSI_BOLD_PURPLE;
            case "DEBUG" -> ANSI_BOLD_BLUE;
            default -> ANSI_BOLD_WHITE;
        };
        return color + levelName + ANSI_RESET;
    }
}
