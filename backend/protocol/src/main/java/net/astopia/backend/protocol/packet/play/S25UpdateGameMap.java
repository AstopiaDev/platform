package net.astopia.backend.protocol.packet.play;

import io.netty.buffer.ByteBuf;
import lombok.*;
import lombok.experimental.FieldDefaults;
import net.astopia.backend.protocol.packet.ChannelProcessorContext;
import net.astopia.backend.protocol.packet.Packet;
import net.astopia.backend.protocol.packet.PacketProcessor;
import net.astopia.backend.protocol.packet.PacketUtils;
import net.astopia.backend.protocol.packet.play.processor.PlayServerProcessor;
import org.jetbrains.annotations.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class S25UpdateGameMap extends Packet<PlayServerProcessor> {

    int arenaId;

    String mapName;

    @Override
    public void process(@NotNull PlayServerProcessor processor, @NotNull ChannelProcessorContext ctx) throws Exception {
        processor.process(this, ctx);
    }

    @Override
    public boolean isProcessor(PacketProcessor processor) {
        return processor instanceof PlayServerProcessor;
    }

    @Override
    public void read0(@NotNull ByteBuf buf) throws Exception {
        this.arenaId = buf.readInt();
        this.mapName = PacketUtils.readString(buf);
    }

    @Override
    public void write0(@NotNull ByteBuf buf) throws Exception {
        buf.writeInt(arenaId);
        PacketUtils.writeString(buf, mapName);
    }

}
