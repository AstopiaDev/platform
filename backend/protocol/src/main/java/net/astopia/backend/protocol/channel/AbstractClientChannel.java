package net.astopia.backend.protocol.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.NonNull;
import net.astopia.api.protocol.platform.IClientChannel;
import net.astopia.api.protocol.platform.IDTO;
import net.astopia.api.protocol.platform.LogMessageDTO;
import net.astopia.api.protocol.platform.Platform;
import net.astopia.backend.protocol.exception.ConnectException;
import net.astopia.backend.protocol.packet.BossProcessor;
import net.astopia.backend.protocol.packet.Packet;
import net.astopia.backend.protocol.packet.PacketDirection;
import net.astopia.backend.protocol.packet.play.LogMessage;
import org.jetbrains.annotations.Nullable;

import java.net.SocketAddress;
import java.nio.channels.AlreadyConnectedException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Getter
public abstract class AbstractClientChannel extends AbstractChannel implements IClientChannel {

    protected EventLoopGroup worker;

    protected Class<? extends SocketChannel> channelClass;

    protected Bootstrap bootstrap;

    private ChannelFuture future;
    private AbstractRemoteServerChannel channel;

    public AbstractClientChannel(SocketAddress address, int threads) {
        super(address, threads);

        init();
    }

    protected void ensureConnectionAvailability() throws ConnectException {
        if (isConnected()) {
            throw new ConnectException(this, new AlreadyConnectedException());
        }
    }

    public void connectAsynchronous() {
        connectAsynchronous(ConnectException::printStackTrace, null);
    }

    public void connectAsynchronous(@NonNull Consumer<ConnectException> errorHandler) {
        connectAsynchronous(errorHandler, null);
    }

    protected boolean reconnecting;

    public void reconnect() {
        if (reconnecting) {
            return;
        }

        reconnecting = true;
        scheduleReconnect();
    }

    protected void scheduleReconnect() {
        Platform.logger.info("Reconnecting in 6 seconds...");
        worker.schedule(this::doReconnect, 6, TimeUnit.SECONDS);
    }

    protected void doReconnect() {
        try {
            Platform.logger.info("Attempting to reconnect xd lmfao");
            connectSynchronized();

            reconnecting = false;

        } catch (Exception e) {
            System.out.println("Unable to reconnect: " + e);

            scheduleReconnect();
        }
    }

    public void connectAsynchronous(@Nullable Runnable success) {
        connectAsynchronous(ConnectException::printStackTrace, success);
    }

    @Override
    public PacketDirection getOutboundPacketDirection() {
        return PacketDirection.TO_SERVER;
    }

    @Override
    public PacketDirection getInboundPacketDirection() {
        return PacketDirection.TO_CLIENT;
    }


    protected abstract AbstractRemoteServerChannel newServerChannel(SocketChannel channel);

    public void connectAsynchronous(@NonNull Consumer<ConnectException> errorHandler, @Nullable Runnable success) {
        try {
            ensureConnectionAvailability();

            future = bootstrap.connect();

            future.addListener(future -> {
                if (future.isSuccess()) {
                    if (success != null) {
                        success.run();
                    }
                } else {
                    errorHandler.accept(new ConnectException(this, future.cause()));
                }

                this.future = null;
            });
        } catch (Exception e) {
            errorHandler.accept(new ConnectException(this, e));
        }
    }

    public void connectSynchronized() throws ConnectException {
        ensureConnectionAvailability();

        try {
            future = bootstrap.connect().sync();

            if (!future.isSuccess()) {
                throw future.cause();
            }
        } catch (Throwable t) {
            throw new ConnectException(this, t);
        } finally {
            future = null;
        }
    }

    protected void closeChannel() {
        channel.close();
    }

    protected void interruptFuture() {
        future.cancel(true);
        future = null;
    }

    public void closeConnection() {
        if (isConnected()) {
            closeChannel();
        } else if (future != null) {
            interruptFuture();
        }
    }

    public boolean isConnected() {
        return channel != null && channel.isActive();
    }

    public boolean isConnecting() {
        return future != null;
    }

    @Override
    protected void init() {
        super.init();

        initChannelClass();
        initEventLoopGroups();
        initBootstrap();
    }

    @Override
    public BossProcessor newPacketProcessor(SocketChannel channel) {
        return this.channel = newServerChannel(channel);
    }

    protected void initChannelClass() {
        channelClass = epoll ? EpollSocketChannel.class : NioSocketChannel.class;
    }

    protected void initEventLoopGroups() {
        worker = epoll
                ? new EpollEventLoopGroup(threads, threadFactory)
                : new NioEventLoopGroup(threads, threadFactory);
    }

    protected void initBootstrap() {
        bootstrap = new Bootstrap()
                .option(ChannelOption.TCP_NODELAY, true)
                .remoteAddress(socketAddress)
                .channel(channelClass)
                .handler(channelInitializer)
                .group(worker);
    }

    public void sendPacket(Packet<?> packet) {
        if (isConnected()) {
            channel.sendPacket(packet);
        }
    }

    public void sendDTO(IDTO dto) {
        if (isConnected()) {
            if (dto instanceof LogMessageDTO) {
                channel.sendPacket(LogMessage.fromDTO((LogMessageDTO) dto));
            } else {
                throw new IllegalArgumentException("Unsupported DTO type: " + dto.getClass().getName());
            }
        }
    }

    public <A extends Packet.Response<?>, B extends Packet.Request<?, A>> CompletableFuture<A> awaitPacket(@NonNull B packet) {
        return channel.awaitPacket(packet);
    }

    public <A extends Packet.Response<?>, B extends Packet.Request<?, A>> CompletableFuture<A> awaitPacket(@NonNull B packet, long timeout) {
        return channel.awaitPacket(packet, timeout);
    }
}
