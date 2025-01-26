package net.astopia.backend.protocol.packet.handshake.processor;

import net.astopia.backend.protocol.packet.ChannelProcessorContext;
import net.astopia.backend.protocol.packet.handshake.Handshake;

/**
 * Processes packet FROM client
 *
 * 
 */
public interface HandshakeClientProcessor extends HandshakeProcessor {

    default void process(Handshake.Request packet, ChannelProcessorContext ctx) {
        // override me
    }

}
