package net.astopia.core.minimind.punishments.protocol

import net.astopia.core.minimind.Minimind

abstract class ProtocolPunishmentsMinimind : Minimind {
    override fun getId(): String {
        return "minimind"
    }
}