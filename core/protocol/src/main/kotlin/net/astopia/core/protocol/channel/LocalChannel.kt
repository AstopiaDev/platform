package net.astopia.core.protocol.channel

import io.netty.channel.ChannelInitializer
import io.netty.channel.epoll.Epoll
import io.netty.channel.socket.SocketChannel
import net.astopia.core.protocol.channel.initializer.CustomProtocolInitializer
import java.net.SocketAddress
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

interface LocalChannel {
    companion object {
        val epoll: Boolean = Epoll.isAvailable()
        val threadFactory = object : ThreadFactory {
            private val counter = AtomicInteger()

            override fun newThread(r: Runnable): Thread {
                return Thread(r, "[Netty] EventLoopGroup #${counter.incrementAndGet()}")
            }
        }
    }

    val channelInitializer: ChannelInitializer<SocketChannel>
        get() = CustomProtocolInitializer(this)
    val socketAddress: SocketAddress
    val threads: Int

    fun initialize() {}
    fun newRemoteChannel(channel: SocketChannel)
}