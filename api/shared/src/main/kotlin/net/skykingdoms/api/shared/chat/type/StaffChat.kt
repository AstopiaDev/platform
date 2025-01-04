package net.skykingdoms.api.shared.chat.type

import net.skykingdoms.api.backend.permission.Group
import net.skykingdoms.api.shared.chat.ServerChat
import net.skykingdoms.api.shared.user.ServerUser

class StaffChat : ServerChat(hashSetOf(Group.JR_HELPER), HashSet(), "[STAFF]") {
    override fun sendMessage(message: String) {
        super.sendMessage(message)
    }

    override fun sendMessage(user: ServerUser, message: String) {
        super.sendMessage(user, message)
    }
}