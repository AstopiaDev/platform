package net.astopia.core.minimind.player.connection

import net.astopia.core.protocol.channel.LocalChannel
import java.net.SocketAddress

class LocalClientConnection(
    override val socketAddress: SocketAddress,
    override val threads: Int
) : LocalChannel {

}