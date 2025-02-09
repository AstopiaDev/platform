package net.astopia.core.megamind

import net.astopia.core.megamind.protocol.LocalServerConnection
import java.net.BindException
import java.net.InetSocketAddress

object Megamind {
    var connection: LocalServerConnection? = null

    fun init() {
        connection = LocalServerConnection(InetSocketAddress("127.0.0.1", 3455), 2)
        try {
            connection!!.bindSynchronized()
        } catch (e: BindException) {
            e.printStackTrace()
            return
        }
    }
}

fun main() {
    Megamind.init()
}