package net.astopia.backend.protocol.packet.handshake.processor;

import net.astopia.backend.protocol.packet.ChannelProcessorContext;
import net.astopia.backend.protocol.packet.handshake.Handshake;

/**
 * Processes packets FROM server
 *
 * 
 */
public interface HandshakeServerProcessor extends HandshakeProcessor {

    default void process(Handshake.Response packet, ChannelProcessorContext ctx) {
        // override me
    }

}
