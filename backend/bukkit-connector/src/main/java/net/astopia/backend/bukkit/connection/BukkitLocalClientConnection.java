package net.astopia.backend.bukkit.connection;

import io.netty.channel.socket.SocketChannel;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.astopia.backend.protocol.channel.AbstractClientChannel;
import net.astopia.backend.protocol.channel.AbstractRemoteServerChannel;

import java.net.SocketAddress;

/**
 * @author Vihara
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class BukkitLocalClientConnection extends AbstractClientChannel {
    public BukkitLocalClientConnection(final @NonNull SocketAddress address, final int threads) {
        super(address, threads);
    }

    @Override
    protected AbstractRemoteServerChannel newServerChannel(SocketChannel channel) {
        return new RemoteServerConnection(this, channel);
    }
}
