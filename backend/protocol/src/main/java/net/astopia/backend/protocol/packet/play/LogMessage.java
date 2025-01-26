package net.astopia.backend.protocol.packet.play;

import io.netty.buffer.ByteBuf;
import lombok.*;
import lombok.experimental.FieldDefaults;
import net.astopia.api.protocol.platform.LogMessageDTO;
import net.astopia.backend.protocol.packet.ChannelProcessorContext;
import net.astopia.backend.protocol.packet.Packet;
import net.astopia.backend.protocol.packet.PacketProcessor;
import net.astopia.backend.protocol.packet.PacketUtils;
import net.astopia.backend.protocol.packet.play.processor.PlayProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.LogRecord;

@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public final class LogMessage extends Packet<PlayProcessor> {
    Level level;
    String message;
    long millis;

    public static LogMessage fromDTO(LogMessageDTO dto) {
        return new LogMessage(dto.level(), dto.message(), dto.millis());
    }

    public LogMessage(LogRecord record) {
        level = record.getLevel();
        message = record.getMessage();
        millis = record.getMillis();
    }

    @Override
    public void process(@NotNull PlayProcessor processor, @NotNull ChannelProcessorContext ctx) throws Exception {
        processor.process(this, ctx);
    }

    @Override
    public boolean isProcessor(PacketProcessor processor) {
        return processor instanceof PlayProcessor;
    }

    @Override
    public void read0(@NotNull ByteBuf buf) throws Exception {
        level = Level.parse(PacketUtils.readString(buf));
        message = PacketUtils.readString(buf);
        millis = buf.readLong();
    }

    @Override
    public void write0(@NotNull ByteBuf buf) throws Exception {
        PacketUtils.writeString(buf, level.toString());
        PacketUtils.writeString(buf, message);
        buf.writeLong(millis);
    }
}
