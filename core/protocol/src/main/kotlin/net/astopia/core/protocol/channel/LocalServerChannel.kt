package net.astopia.core.protocol.channel

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFactory
import io.netty.channel.ChannelFuture
import io.netty.channel.EventLoopGroup
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollServerSocketChannel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.ServerSocketChannel
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.util.concurrent.Future
import io.netty.util.concurrent.GenericFutureListener
import java.net.BindException
import java.net.SocketAddress
import java.nio.channels.AlreadyBoundException
import java.util.function.Consumer

abstract class LocalServerChannel(override var socketAddress: SocketAddress, override var threads: Int) : LocalChannel {
    var boss: EventLoopGroup? = null
    var worker: EventLoopGroup? = null
    var channelFactory: ChannelFactory<ServerSocketChannel>? = null
    var bootstrap: ServerBootstrap? = null
    var future: ChannelFuture? = null
    var channel: ServerSocketChannel? = null

    init {
        initialize()
    }

    override fun initialize() {
        super.initialize()

        initChannelFactory()
        initEventLoopGroups()
        initBootstrap()
    }

    protected abstract fun newClientChannel(channel: SocketChannel?): RemoteClientChannel?

    override fun newRemoteChannel(channel: SocketChannel) {
        newClientChannel(channel)
    }

    protected fun initChannelFactory() {
        channelFactory =
            if (LocalChannel.epoll) ChannelFactory {
                EpollServerSocketChannel()
            }
            else ChannelFactory {
                NioServerSocketChannel()
            }
    }

    protected fun initEventLoopGroups() {
        if (LocalChannel.epoll) {
            boss = EpollEventLoopGroup(2, LocalChannel.threadFactory)
            worker = EpollEventLoopGroup(LocalChannel.threadFactory)
        } else {
            boss = NioEventLoopGroup(2, LocalChannel.threadFactory)
            worker = NioEventLoopGroup(LocalChannel.threadFactory)
        }
    }

    protected fun initBootstrap() {
        bootstrap = ServerBootstrap()
            .localAddress(socketAddress)
            .channelFactory(channelFactory)
            .childHandler(channelInitializer)
            .group(boss, worker)
    }

    protected fun closeChannel() {
        channel!!.close()
    }

    protected fun interruptFuture() {
        future!!.cancel(true)
        future = null
    }

    @Throws(AlreadyBoundException::class)
    protected fun checkBindAvailability() {
        if (isConnected()) {
            throw AlreadyBoundException()
        }
    }

    fun bindAsynchronous() {
        bindAsynchronous(
            Consumer { obj: BindException? -> obj?.printStackTrace() },
            null
        )
    }

    fun bindAsynchronous(errorHandler: Consumer<BindException?>) {
        bindAsynchronous(errorHandler, null)
    }

    fun bindAsynchronous(success: Runnable?) {
        bindAsynchronous(
            Consumer { obj: BindException? -> obj?.printStackTrace() },
            success
        )
    }

    fun bindAsynchronous(
        errorHandler: Consumer<BindException?>,
        success: Runnable?
    ) {
        try {
            checkBindAvailability()

            future = bootstrap!!.bind()

            future!!.addListener(GenericFutureListener { future: Future<in Void?>? ->
                if (future!!.isSuccess()) {
                    channel = this.future!!.channel() as ServerSocketChannel?

                    if (success != null) {
                        success.run()
                    }
                } else {
                    errorHandler.accept(BindException())
                }
                this.future = null
            })
        } catch (e: Exception) {
            errorHandler.accept(BindException())
        }
    }

    @Throws(BindException::class)
    fun bindSynchronized() {
        checkBindAvailability()

        try {
            future = bootstrap!!.bind().sync()

            if (future!!.isSuccess()) {
                channel = future!!.channel() as ServerSocketChannel?
            } else {
                throw future!!.cause()
            }
        } catch (t: Throwable) {
            throw BindException()
        } finally {
            future = null
        }
    }

    fun closeConnection() {
        if (isConnected()) {
            closeChannel()
        } else if (future != null) {
            interruptFuture()
        }
    }

    fun isConnected(): Boolean {
        return channel != null && channel!!.isActive()
    }
}