package net.astopia.core.protocol.channel

import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollSocketChannel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.util.concurrent.Future
import io.netty.util.concurrent.GenericFutureListener
import java.net.ConnectException
import java.net.SocketAddress
import java.nio.channels.AlreadyConnectedException
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

abstract class LocalClientChannel(override val socketAddress: SocketAddress, override val threads: Int) : LocalChannel {
    var worker: EventLoopGroup? = null
    var channelClass: Class<out SocketChannel>? = null
    var bootstrap: Bootstrap? = null
    var future: ChannelFuture? = null
    var remoteServerChannel: RemoteServerChannel? = null

    init {
        initialize()
    }

    @Throws(AlreadyConnectedException::class)
    protected fun ensureConnectionAvailability() {
        if (isConnected()) {
            throw AlreadyConnectedException()
        }
    }

    fun connectAsynchronous() {
        connectAsynchronous(
            Consumer { obj: ConnectException? -> obj?.printStackTrace() },
            null
        )
    }

    fun connectAsynchronous(errorHandler: Consumer<ConnectException?>) {
        connectAsynchronous(errorHandler, null)
    }

    protected var reconnecting: Boolean = false

    fun reconnect() {
        if (reconnecting) {
            return
        }

        reconnecting = true
        scheduleReconnect()
    }

    protected fun scheduleReconnect() {
        println("Reconnecting in 6 seconds...")
        worker!!.schedule(Runnable { this.doReconnect() }, 6, TimeUnit.SECONDS)
    }

    protected fun doReconnect() {
        try {
            println("Attempting to reconnect xd lmfao")
            connectSynchronized()

            reconnecting = false
        } catch (e: java.lang.Exception) {
            println("Unable to reconnect: " + e)

            scheduleReconnect()
        }
    }

    protected abstract fun newServerChannel(channel: SocketChannel?): RemoteServerChannel?

    override fun newRemoteChannel(channel: SocketChannel) {
        newServerChannel(channel)
    }

    fun connectAsynchronous(success: Runnable?) {
        connectAsynchronous(
            Consumer { obj: ConnectException? -> obj?.printStackTrace() },
            success
        )
    }


    fun connectAsynchronous(
        errorHandler: Consumer<ConnectException?>,
        success: Runnable?
    ) {
        try {
            ensureConnectionAvailability()

            future = bootstrap!!.connect()

            future!!.addListener(GenericFutureListener { future: Future<in Void?>? ->
                if (future!!.isSuccess()) {
                    if (success != null) {
                        success.run()
                    }
                } else {
                    errorHandler.accept(ConnectException())
                }
                this.future = null
            })
        } catch (e: Exception) {
            errorHandler.accept(ConnectException())
        }
    }

    @Throws(ConnectException::class)
    fun connectSynchronized() {
        ensureConnectionAvailability()

        try {
            future = bootstrap!!.connect().sync()

            if (!future!!.isSuccess()) {
                throw future!!.cause()
            }
        } catch (t: Throwable) {
            throw ConnectException()
        } finally {
            future = null
        }
    }

    override fun initialize() {
        super.initialize()

        initChannelClass()
        initEventLoopGroups()
        initBootstrap()
    }

    protected fun closeChannel() {
        remoteServerChannel?.close()
    }

    protected fun interruptFuture() {
        future!!.cancel(true)
        future = null
    }

    fun closeConnection() {
        if (isConnected()) {
            closeChannel()
        } else if (future != null) {
            interruptFuture()
        }
    }

    protected fun initChannelClass() {
        channelClass =
            if (LocalChannel.epoll) EpollSocketChannel::class.java else NioSocketChannel::class.java
    }

    protected fun initEventLoopGroups() {
        worker = if (LocalChannel.epoll)
            EpollEventLoopGroup(threads, LocalChannel.threadFactory)
        else
            NioEventLoopGroup(threads, LocalChannel.threadFactory)
    }

    protected fun initBootstrap() {
        bootstrap = Bootstrap()
            .option<Boolean?>(ChannelOption.TCP_NODELAY, true)
            .remoteAddress(socketAddress)
            .channel(channelClass)
            .handler(channelInitializer)
            .group(worker)
    }

    fun isConnected(): Boolean {
        return remoteServerChannel != null && remoteServerChannel?.isActive() == true
    }

    fun isConnecting(): Boolean {
        return future != null
    }

    fun isReconnecting(): Boolean {
        return reconnecting
    }
}