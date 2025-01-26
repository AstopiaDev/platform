package net.astopia.backend.protocol.server;

/**
 * Indicates that this remote can be deployed and shut down
 * by a node server driver.
 */
public interface IDeployable extends IConnectable {

    INodeServer getNode();

}
