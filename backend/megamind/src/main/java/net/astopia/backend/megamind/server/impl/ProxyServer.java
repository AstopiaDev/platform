package net.astopia.backend.megamind.server.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.astopia.backend.megamind.connection.RemoteClientConnection;
import net.astopia.backend.megamind.server.AbstractPlayerHolder;
import net.astopia.backend.protocol.server.IDeployable;

import java.net.InetSocketAddress;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public final class ProxyServer extends AbstractPlayerHolder implements IDeployable {

    NodeServer node;

    /**
     * Remote proxy's host
     */
    InetSocketAddress host;

    public ProxyServer(final @NonNull String name,
                       final @NonNull NodeServer server,
                       final @NonNull RemoteClientConnection connection,
                       final @NonNull InetSocketAddress remote) {
        super(name, connection);
        this.node = server;
        this.host = remote;
    }

    // TODO: Proxy geo position method...

}
