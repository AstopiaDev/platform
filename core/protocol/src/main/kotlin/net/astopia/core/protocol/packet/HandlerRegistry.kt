package net.astopia.core.protocol.packet

import com.google.protobuf.MessageLite
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelId
import io.netty.channel.socket.SocketChannel
import net.astopia.core.proto.Protocol.*
import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicLong
import kotlin.reflect.KClass

object HandlerRegistry {
    private data class PacketHandlerEntry<T : MessageLite>(
        val handler: PacketHandler<T>,
        val weight: Int,
        val id: Long = idCounter.getAndIncrement()
    ) : Comparable<PacketHandlerEntry<*>> {
        override fun compareTo(other: PacketHandlerEntry<*>): Int {
            return when {
                other.weight != weight -> other.weight - weight
                else -> other.id.compareTo(id)
            }
        }
    }

    private data class LifecycleHandlerEntry(
        val handler: (ChannelHandlerContext) -> Unit,
        val weight: Int,
        val id: Long = idCounter.getAndIncrement()
    ) : Comparable<LifecycleHandlerEntry> {
        override fun compareTo(other: LifecycleHandlerEntry): Int {
            return when {
                other.weight != weight -> other.weight - weight
                else -> other.id.compareTo(id)
            }
        }
    }

    private val idCounter = AtomicLong(0)
    private val packetHandlers = ConcurrentHashMap<Class<*>, MutableSet<PacketHandlerEntry<*>>>()
    private val connectHandlers = ConcurrentHashMap.newKeySet<LifecycleHandlerEntry>()
    private val disconnectHandlers = ConcurrentHashMap.newKeySet<LifecycleHandlerEntry>()
    private val connectionAttributes = ConcurrentHashMap<ChannelId, MutableMap<String, Any>>()

    class PropagationContext {
        var stopPropagation: Boolean = false
            private set

        fun stop() {
            stopPropagation = true
        }
    }

    fun <T : MessageLite> registerHandler(
        packetClass: KClass<T>,
        weight: Int = 0,
        handler: PacketHandler<T>
    ) {
        packetHandlers.compute(packetClass.java) { _, set ->
            (set ?: ConcurrentHashMap.newKeySet()).apply {
                add(PacketHandlerEntry(handler, weight))
            }
        }
    }

    fun onConnect(weight: Int = 0, handler: (ChannelHandlerContext) -> Unit) {
        connectHandlers.add(LifecycleHandlerEntry(handler, weight))
    }

    fun onDisconnect(weight: Int = 0, handler: (ChannelHandlerContext) -> Unit) {
        disconnectHandlers.add(LifecycleHandlerEntry(handler, weight))
    }

    fun getAttributes(ctx: ChannelHandlerContext): MutableMap<String, Any> {
        return connectionAttributes.computeIfAbsent(ctx.channel().id()) { mutableMapOf() }
    }

    internal fun handlePacket(ctx: ChannelHandlerContext, packet: MessageLite) {
        val propagation = PropagationContext()
        packetHandlers[packet::class.java]?.sorted()?.forEach { entry ->
            if (!propagation.stopPropagation) {
                try {
                    @Suppress("UNCHECKED_CAST")
                    (entry.handler as PacketHandler<MessageLite>).handle(ctx, packet, propagation)
                } catch (e: Exception) {
                    println("Error in packet handler: ${e.message}")
                }
            }
        }
    }

    internal fun handleResponse(ctx: ChannelHandlerContext, responseEnvelope: ResponseEnvelope) {
        val channelId = ctx.channel().id()
        val requestId = responseEnvelope.requestId
        println("Response with request id: " + requestId)
        val pendingRequestMap = pendingRequests[channelId]

        if (pendingRequestMap != null) {
            val future = pendingRequestMap[requestId]
            if (future != null) {
                println("Future aint null g!")
                try {
                    if (!responseEnvelope.hasDirect() || responseEnvelope.direct) {
                        val commonResponsePacket = Packet.parseFrom(responseEnvelope.response)
                        println("with type" + commonResponsePacket.type)
                        val responsePacket = PacketRegistry.parsePacket(
                            commonResponsePacket.type,
                            commonResponsePacket.data.toByteArray()
                        )
                        // TODO: tle se zalomi

                        println("response packet isss " + responsePacket.javaClass.simpleName)
                        future.complete(responsePacket)
                    } else {
                        val commonResponsePacket = Packet.parseFrom(responseEnvelope.response)
                        println("RESPONDINGGGGG")
                        future.complete(commonResponsePacket)
                    }
                } catch (e: Exception) {
                    future.completeExceptionally(e)
                } finally {
                    cleanupRequest(channelId, requestId)
                }
            }
        }
    }

    internal fun handleConnect(ctx: ChannelHandlerContext) {
        val propagation = PropagationContext()
        connectHandlers.sorted().forEach { entry ->
            if (!propagation.stopPropagation) {
                try {
                    entry.handler(ctx)
                } catch (e: Exception) {
                    println("Error in connect handler: ${e.message}")
                }
            }
        }
    }

    internal fun handleDisconnect(ctx: ChannelHandlerContext) {
        val propagation = PropagationContext()
        disconnectHandlers.sorted().forEach { entry ->
            if (!propagation.stopPropagation) {
                try {
                    entry.handler(ctx)
                } catch (e: Exception) {
                    println("Error in disconnect handler: ${e.message}")
                }
            }
        }

        connectionAttributes.remove(ctx.channel().id())

        pendingRequests.remove(ctx.channel().id())?.forEach { (_, future) ->
            future.completeExceptionally(InterruptedException())
        }
    }

    private val requestIdGenerator = AtomicLong(0)
    private val pendingRequests = ConcurrentHashMap<ChannelId, MutableMap<Long, CompletableFuture<MessageLite>>>()

    internal fun dispatch(
        ctx: ChannelHandlerContext,
        payload: MessageLite,
        propagation: PropagationContext,
        responder: (MessageLite) -> Unit
    ) {
        val packetClass = payload::class.java
        packetHandlers[packetClass]?.sorted()?.forEach { entry ->
            if (!propagation.stopPropagation) {
                try {
                    @Suppress("UNCHECKED_CAST")
                    (entry.handler as PacketHandler<MessageLite>).handle(
                        ctx,
                        payload,
                        propagation,
                        responder
                    )
                } catch (e: Exception) {
                    println("Error in packet handler: ${e.message}")
                }
            }
        }
    }

    fun sendRequest(channel: SocketChannel, request: MessageLite): CompletableFuture<MessageLite> {
        val requestId = requestIdGenerator.getAndIncrement()
        val future = CompletableFuture<MessageLite>()

        pendingRequests
            .computeIfAbsent(channel.id()) { ConcurrentHashMap() }
            .put(requestId, future)

        val wrappedRequest: Packet = Packet.newBuilder()
            .setType(request.javaClass.simpleName)
            .setData(request.toByteString())
            .build()

        val envelope = RequestEnvelope.newBuilder()
            .setRequestId(requestId)
            .setRequest(wrappedRequest.toByteString())
            .build()

        channel.writeAndFlush(envelope).addListener {
            if (!it.isSuccess) {
                future.completeExceptionally(it.cause())
                cleanupRequest(channel.id(), requestId)
            }
        }


        return future
    }

    fun sendRequest(channel: SocketChannel, request: RequestEnvelope): CompletableFuture<MessageLite> {
        val requestId = requestIdGenerator.getAndIncrement()
        val future = CompletableFuture<MessageLite>()

        pendingRequests
            .computeIfAbsent(channel.id()) { ConcurrentHashMap() }
            .put(requestId, future)

        val envelope = request.toBuilder().setRequestId(requestId).build()

        println("Sendinggggggg envelope with request id " + requestId)

        channel.writeAndFlush(envelope).addListener {
            if (!it.isSuccess) {
                future.completeExceptionally(it.cause())
                cleanupRequest(channel.id(), requestId)
            }
        }

        return future
    }

    fun sendRequest(channel: SocketChannel, request: MessageLite, minimindId: String, direct: Boolean = true): CompletableFuture<MessageLite> {
        val requestId = requestIdGenerator.getAndIncrement()
        val future = CompletableFuture<MessageLite>()

        pendingRequests
            .computeIfAbsent(channel.id()) { ConcurrentHashMap() }
            .put(requestId, future)

        val wrappedRequest: Packet = Packet.newBuilder()
            .setType(request.javaClass.simpleName)
            .setData(request.toByteString())
            .build()

        val envelope = RequestEnvelope.newBuilder()
            .setRequestId(requestId)
            .setRequest(wrappedRequest.toByteString())
            .setMinimind(minimindId)
            .setDirect(direct)
            .build()

        println("Sending packet to megamind with requeqst id " + requestId)

        channel.writeAndFlush(envelope).addListener {
            if (!it.isSuccess) {
                future.completeExceptionally(it.cause())
                cleanupRequest(channel.id(), requestId)
            }
        }


        return future
    }

    fun sendPacket(channel: SocketChannel, packet: MessageLite) {
        channel.writeAndFlush(packet, channel.voidPromise())
    }

    fun sendRequestWithTimeout(
        channel: SocketChannel,
        request: MessageLite,
        timeout: Duration = Duration.ofSeconds(30)
    ): CompletableFuture<MessageLite> {
        val requestId = requestIdGenerator.getAndIncrement()
        val future = sendRequest(channel, request)

        channel.eventLoop().schedule({
            if (!future.isDone) {
                future.completeExceptionally(TimeoutException())
                cleanupRequest(channel.id(), requestId)
            }
        }, timeout.toMillis(), TimeUnit.MILLISECONDS)
        return future
    }

    private fun cleanupRequest(channelId: ChannelId, requestId: Long) {
        pendingRequests[channelId]?.remove(requestId)
    }
}