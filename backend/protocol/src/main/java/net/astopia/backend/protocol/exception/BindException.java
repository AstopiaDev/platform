package net.astopia.backend.protocol.exception;

import net.astopia.backend.protocol.channel.AbstractChannel;

public class BindException extends Exception {

    public BindException(AbstractChannel channel, Throwable cause) {
        super("Unable to bind server [" + channel.getSocketAddress() + "]", cause);
    }

}
