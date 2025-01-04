package net.skykingdoms.api.shared.chat

import net.skykingdoms.api.backend.permission.Group
import net.skykingdoms.api.shared.user.ServerUser

interface Chat {
    fun groups() : HashSet<Group>?
    fun participants() : HashSet<ServerUser>
    fun prefix(): String?
    fun sendMessage(message: String)
    fun sendMessage(user: ServerUser, message: String)
}