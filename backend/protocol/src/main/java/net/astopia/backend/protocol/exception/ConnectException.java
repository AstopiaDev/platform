package net.astopia.backend.protocol.exception;

import net.astopia.backend.protocol.channel.AbstractChannel;

public class ConnectException extends Exception {

    public ConnectException(AbstractChannel channel, Throwable cause) {
        super("Unable to connect to server [" + channel.getSocketAddress() + "]", cause);
    }

}
