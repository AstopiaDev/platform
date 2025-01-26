package net.astopia.api.protocol.platform;

import java.util.logging.Level;

public record LogMessageDTO(Level level, String message, long millis) implements IDTO {
}