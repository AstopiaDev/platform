package net.astopia.backend.megamind.server.impl;

import lombok.NonNull;
import net.astopia.backend.megamind.connection.RemoteClientConnection;
import net.astopia.backend.megamind.server.AbstractRemoteConnectable;
import net.astopia.backend.protocol.server.IConnectable;

public class ServiceServer extends AbstractRemoteConnectable implements IConnectable {
    public ServiceServer(final @NonNull String name,
                      final @NonNull RemoteClientConnection connection) {
        super(name, connection);
    }
}