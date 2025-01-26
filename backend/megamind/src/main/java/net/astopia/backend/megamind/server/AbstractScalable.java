package net.astopia.backend.megamind.server;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.val;
import net.astopia.api.protocol.server.IScalable;
import net.astopia.api.protocol.server.impl.ScalableDataImpl;
import net.astopia.backend.megamind.connection.RemoteClientConnection;
import net.astopia.backend.megamind.data.gamer.Gamer;
import net.astopia.backend.megamind.lookup.LookupManager;
import net.astopia.backend.protocol.server.IDeployable;
import net.astopia.backend.protocol.server.IPlayableBukkitServer;


@Getter
public abstract class AbstractScalable extends AbstractPlayerHolder implements IScalable, IDeployable, IPlayableBukkitServer {

    /**
     * Indicates that we had this server instance deployed
     * by /testflow command.
     * <p>
     * This field is required for testflow manager
     */
    @Setter
    int linkedTestFlowId;

    public AbstractScalable(final @NonNull String name,
                            final @NonNull RemoteClientConnection connection) {
        super(name, connection);
    }

    public ScalableDataImpl bakeServerData() {
        return new ScalableDataImpl(getServerScale(), getGameType(), getGameMode());
    }

    @Override
    public boolean addPlayer(@NonNull Gamer gamer) {
        val added = super.addPlayer(gamer);
        if (added) {
            LookupManager.INSTANCE.handleOnlineChange(this);
        }

        return added;
    }

    @Override
    public boolean removePlayer(@NonNull Gamer gamer) {
        val removed = super.removePlayer(gamer);
        if (removed) {
            LookupManager.INSTANCE.handleOnlineChange(this);
        }

        return removed;
    }
}
