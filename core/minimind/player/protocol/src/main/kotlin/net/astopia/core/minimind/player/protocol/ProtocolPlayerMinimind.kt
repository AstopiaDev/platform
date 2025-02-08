package net.astopia.core.minimind.player.protocol

import net.astopia.core.minimind.Minimind

abstract class ProtocolPlayerMinimind : Minimind {
    override fun getId(): String {
        return "minimind"
    }
}