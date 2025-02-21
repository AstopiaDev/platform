package net.astopia.core.megamind.protocol

import io.netty.channel.WriteBufferWaterMark
import io.netty.channel.socket.SocketChannel
import io.netty.util.internal.SystemPropertyUtil
import net.astopia.core.protocol.channel.LocalServerChannel
import net.astopia.core.protocol.channel.RemoteClientChannel
import net.astopia.core.protocol.packet.HandlerRegistry
import java.net.SocketAddress

class LocalServerConnection(socketAddress: SocketAddress, threads: Int) : LocalServerChannel(socketAddress, threads) {
    companion object {
        var LOW_MARK: Int = SystemPropertyUtil.getInt("net.astopia.internal.low_mark", 2 shl 18) // 0.5 mb
        var HIGH_MARK: Int = SystemPropertyUtil.getInt("net.astopia.internal.high_mark", 2 shl 20) // 2 mb
        var MARK: WriteBufferWaterMark = WriteBufferWaterMark(LOW_MARK, HIGH_MARK)
    }

    override fun newClientChannel(channel: SocketChannel?): RemoteClientChannel? {
        return RemoteClientConnection(channel!!)
    }

    init {
        HandlerRegistry.onConnect(1) { ctx ->
            println("Connected!!")
        }
    }
}