package net.astopia.api.protocol.platform;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class PacketLoggerHandler extends Handler {
    @Override
    public void publish(@NotNull LogRecord record) {
        LogMessageDTO dto = new LogMessageDTO(record.getLevel(), record.getMessage(), record.getMillis());
        Platform.channel.sendDTO(dto);
    }

    @Override
    public void flush() {}

    @Override
    public void close() throws SecurityException {}
}
