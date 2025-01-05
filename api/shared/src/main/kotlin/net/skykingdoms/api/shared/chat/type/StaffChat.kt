package net.skykingdoms.api.shared.chat.type

import net.kyori.adventure.text.Component
import net.skykingdoms.api.backend.permission.Group
import net.skykingdoms.api.shared.chat.ServerChat
import net.skykingdoms.api.shared.user.ServerUser

class StaffChat : ServerChat(hashSetOf(Group.JR_HELPER), HashSet(), "[STAFF]") {
    override fun prefixComponent(): Component? {
        if (prefix != null) {
            return Component.text(prefix)
        }

        return null
    }

    override fun sendMessage(message: String) {
        super.sendMessage(message)
    }

    override fun sendMessage(message: Component) {
        super.sendMessage(message)
    }

    override fun sendMessage(user: ServerUser, message: String) {
        super.sendMessage(user, message)
    }

    override fun sendMessage(
        user: ServerUser,
        message: Component
    ) {
        TODO("Not yet implemented")
    }
}