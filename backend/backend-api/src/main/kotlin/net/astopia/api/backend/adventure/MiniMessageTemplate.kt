package net.astopia.api.backend.adventure

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

object MiniMessageTemplate {
    val MM: (String) -> Component = { inputString ->
        val mm = MiniMessage.miniMessage()
        mm.deserialize(inputString)
    }
}
