package net.astopia.backend.minestom.connection

import io.netty.channel.socket.SocketChannel
import net.minestom.server.MinecraftServer
import net.astopia.api.protocol.platform.Platform
import net.astopia.api.protocol.type.RemoteConnectedType
import net.astopia.backend.minestom.HeartBeat
import net.astopia.backend.minestom.MinestomConnector
import net.astopia.backend.minestom.event.ConnectionEstablishedEvent
import net.astopia.backend.minestom.event.InstanceActiveEvent
import net.astopia.backend.minestom.event.InstanceDataUpdateEvent
import net.astopia.backend.minestom.event.InstanceInactiveEvent
import net.astopia.backend.protocol.channel.AbstractClientChannel
import net.astopia.backend.protocol.channel.AbstractRemoteServerChannel
import net.astopia.backend.protocol.packet.ChannelProcessorContext
import net.astopia.backend.protocol.packet.PacketProtocol
import net.astopia.backend.protocol.packet.handshake.Handshake
import net.astopia.backend.protocol.packet.play.S04UpdateRemoteData
import net.astopia.backend.protocol.packet.play.S11ServerCommand
import net.astopia.backend.protocol.packet.play.SecurityToken
import net.astopia.backend.protocol.type.reconnect.impl.FluentBukkitDataImpl
import java.util.EnumSet

class RemoteServerConnection(local: AbstractClientChannel?, channel: SocketChannel?) :
    AbstractRemoteServerChannel(local, channel) {

    override fun onServerDisconnect() {
        HeartBeat.callEvent(InstanceInactiveEvent())
        MinestomConnector.reconnect = true
        Platform.logger.info("Disconnected")
    }

    override fun active() {
        HeartBeat.callEvent(InstanceActiveEvent(this))

        val flags: EnumSet<Handshake.RequestFlag> = EnumSet.of<Handshake.RequestFlag>(Handshake.RequestFlag.HAS_FLUENT_DATA)
        if (MinestomConnector.reconnect) {
            flags.add(Handshake.RequestFlag.IS_RECONNECT)
        }
        if (MinestomConnector.testFlowId > -1) {
            flags.add(Handshake.RequestFlag.IS_TESTFLOW)
        }

        Platform.logger.info("Sending handshake request")

        sendPacket(Handshake.Request(
            RemoteConnectedType.SERVER,
            MinestomConnector.serverConfig.secret,
            MinestomConnector.serverConfig.transactionId,
            flags,
            MinestomConnector.instanceData,
            MinestomConnector.testFlowId
        ))
    }

    override fun process(packet: Handshake.Response?, ctx: ChannelProcessorContext?) {
        if (packet?.result == Handshake.Result.FAILED) {
            Platform.logger.error("[HANDSHAKE] Unable to connect to coordinator: {}", packet.message)
            return
        }

        upgradeConnection(PacketProtocol.PLAY)

        Platform.logger.info("[HANDSHAKE] Upgraded connection to PLAY")

        Connection.awaitPacket(SecurityToken.Request())?.whenComplete { response, throwable ->
            if (throwable != null) {
                Platform.logger.stacktrace(throwable, "Exception raised on token request! Shutting down the server...");
                MinecraftServer.getServer().stop()
                return@whenComplete
            }

            HeartBeat.callEvent(ConnectionEstablishedEvent(this))
            MinestomConnector.reconnect = false
        }
    }

    override fun process(packet: S11ServerCommand, ctx: ChannelProcessorContext?) {
        MinecraftServer.getCommandManager().executeServerCommand(packet.command)
    }

    override fun process(packet: S04UpdateRemoteData, ctx: ChannelProcessorContext?) {
        val updatedData = packet.serverDataUpdate as FluentBukkitDataImpl
        val currentData = MinestomConnector.instanceData

        currentData.serverName = updatedData.serverName
        currentData.nodeName = updatedData.nodeName

        HeartBeat.callEvent(InstanceDataUpdateEvent(updatedData))
        Platform.logger.info("Updated instance data: {} @ {}", currentData.serverName, currentData.nodeName)
    }
}