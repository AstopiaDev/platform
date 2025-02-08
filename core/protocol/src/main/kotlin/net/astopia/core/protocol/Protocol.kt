package net.astopia.core.protocol

import io.netty.channel.socket.SocketChannel
import net.astopia.core.protocol.channel.LocalChannel

object Protocol {
    private val framer: String = "packet-framer"
    private val encoder: String = "packet-encoder"
    private val decoder: String = "packet-decoder"
    private val handler: String = "packet-handler"
    private val inLog: String = "packet-inbound-logger"
    private val outLog: String = "packet-outbound-logger"

    fun injectIntoPipeline(localChannel: LocalChannel,
                       socketChannel: SocketChannel) {
        val pipeline = socketChannel.pipeline()

    }
}