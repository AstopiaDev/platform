package net.astopia.backend.protocol.server.deploy;

import net.astopia.api.protocol.packet.serialize.Serializable;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


public interface IDeployRequest extends Serializable<IDeployRequest> {

    /**
     * Unique ID of deployment request (transaction)
     */
    @NotNull UUID getTransactionId();

}
