package net.astopia.core.protocol.nats

import com.google.protobuf.MessageLite
import io.nats.client.Connection
import io.nats.client.Dispatcher
import io.nats.client.Nats
import net.astopia.core.protocol.serialization.ProtoSerializer
import java.util.concurrent.CompletableFuture

object NatsClient {
    var connection: Connection? = null
    val serializer = ProtoSerializer()
    val dispatchers = mutableListOf<Dispatcher?>()

    fun connect(serverUrl: String) {
        // todo: JWT
        connection = Nats.connect(serverUrl)
/*        subscribe<HandshakeRequest>("test") { replyTo, request ->
            publish(replyTo, HandshakeResponse.newBuilder().setMessage("HEYY HEYYY").build())
        }*/

        /*request<HandshakeRequest, HandshakeResponse>("proxy_minimind.get_optimal_proxy", HandshakeRequest.newBuilder().setToken("kurc").build()).thenAccept { resp ->
            println("YAYY response ${resp?.message}")
        }

        publish("proxy_minimind.get_optimal_proxy", HandshakeRequest.newBuilder().setToken("kurc").build())*/
    }

    inline fun <reified T : MessageLite> publish(subject: String, message: T) {
        connection?.publish(subject, serializer.serialize(message))
    }

    inline fun <reified T : MessageLite> subscribe(subject: String, noinline callback: (replyTo: String, T) -> Unit) {
        val dispatcher = connection?.createDispatcher { msg ->
            callback(msg.replyTo, serializer.deserialize(msg.data))
        }

        dispatcher?.subscribe(subject)
        dispatchers.add(dispatcher)
    }

    inline fun <reified Req : MessageLite, reified Res: MessageLite> request(subject: String, request: Req): CompletableFuture<Res?> {
        return connection?.request(subject, serializer.serialize(request))?.thenApply { msg ->
            return@thenApply serializer.deserialize<Res>(msg.data)
        } ?: CompletableFuture.completedFuture(null)
    }

    fun close() {
        connection?.close()
    }
}