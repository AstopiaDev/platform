package net.skykingdoms.api.shared.chat

import net.skykingdoms.api.backend.permission.Group
import net.skykingdoms.api.shared.chat.type.StaffChat
import net.skykingdoms.api.shared.user.ServerUser

open class ServerChat(val groups: HashSet<Group>?, val participants: HashSet<ServerUser>, val prefix: String?) : Chat {
    companion object {
        private val chats: HashMap<String, Chat> = HashMap()
        var defaultChat: Chat = ServerChat(null, HashSet(), null)

        fun init() {
            chats.put("default", defaultChat)
            chats.put("staff", StaffChat())
        }
    }

    override fun groups(): HashSet<Group>? {
        return groups
    }

    override fun participants(): HashSet<ServerUser> {
        return participants
    }

    override fun prefix(): String? {
        return prefix
    }

    override fun sendMessage(message: String) {
        for (player in participants) {
            player.sendMessage(message)
        }
    }

    override fun sendMessage(user: ServerUser, message: String) {
        sendMessage(user.language.getMessage("global_server_chat", "%display_name%", user.displayNameAsString, "%message%", message))
    }
}