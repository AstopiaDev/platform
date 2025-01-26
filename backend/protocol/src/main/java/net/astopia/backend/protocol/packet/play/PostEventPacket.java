package net.astopia.backend.protocol.packet.play;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.astopia.api.megamind.event.Event;
import net.astopia.api.protocol.util.PacketUtils;
import net.astopia.backend.protocol.packet.ChannelProcessorContext;
import net.astopia.backend.protocol.packet.Packet;
import net.astopia.backend.protocol.packet.PacketProcessor;
import net.astopia.backend.protocol.packet.play.processor.PlayProcessor;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostEventPacket extends Packet<PlayProcessor> {
    Class<? extends Event> eventClass;
    Event event;

    @Override
    public void process(@NotNull PlayProcessor processor, @NotNull ChannelProcessorContext ctx) throws Exception {
        processor.process(this, ctx);
    }

    @Override
    public boolean isProcessor(PacketProcessor processor) {
        return processor instanceof PlayProcessor;
    }

    @Override
    protected void read0(@NotNull ByteBuf buf) throws Exception {
        this.eventClass = (Class<? extends Event>) Class.forName(PacketUtils.readString(buf));
        this.event = eventClass.getConstructor().newInstance().deserialize(buf);
    }

    @Override
    protected void write0(@NotNull ByteBuf buf) throws Exception {
        PacketUtils.writeString(buf, eventClass.getName());
        this.event.serialize(buf);
    }
}
