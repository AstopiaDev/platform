package net.astopia.backend.megamind.server.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.astopia.backend.megamind.connection.RemoteClientConnection;
import net.astopia.backend.megamind.server.AbstractRemoteConnectable;
import net.astopia.backend.protocol.server.INodeServer;

/**
 * Represents connection to remote NODE driver.
 *
 * 
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public final class NodeServer extends AbstractRemoteConnectable implements INodeServer {

    // TODO: priority system
    //int priority = 0;

    public NodeServer(final @NonNull String name,
                      final @NonNull RemoteClientConnection connection) {
        super(name, connection);
    }

}
