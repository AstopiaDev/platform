package net.skykingdoms.api.shared.chat

import net.kyori.adventure.text.Component
import net.skykingdoms.api.backend.permission.Group
import net.skykingdoms.api.shared.user.ServerUser

interface Chat {
    fun groups() : HashSet<Group>?
    fun participants() : HashSet<ServerUser>
    fun prefix(): String?
    fun prefixComponent(): Component?
    fun sendMessage(message: String)
    fun sendMessage(message: Component)
    fun sendMessage(user: ServerUser, message: String)
    fun sendMessage(user: ServerUser, message: Component)
}