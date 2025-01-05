package net.skykingdoms.api.shared.chat

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.PlayerChatEvent
import net.skykingdoms.api.backend.permission.Group
import net.skykingdoms.api.shared.GameServer
import net.skykingdoms.api.shared.chat.type.StaffChat
import net.skykingdoms.api.shared.user.ServerUser

open class ServerChat(val groups: HashSet<Group>?, val participants: HashSet<ServerUser>, val prefix: String?) : Chat {
    companion object {
        private val chats: HashMap<String, Chat> = HashMap()
        var defaultChat: Chat = ServerChat(null, HashSet(), null)

        fun init(globalEventHandler: GlobalEventHandler) {
            chats.put("default", defaultChat)
            chats.put("staff", StaffChat())

            globalEventHandler.addListener(PlayerChatEvent::class.java) { event ->
                val player = event.player as ServerUser
                player.chat.sendMessage(player, Component.text(event.rawMessage))
                event.isCancelled = true
            }
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

    override fun prefixComponent(): Component? {
        if (prefix != null) {
            return Component.text(prefix)
        }
        return null
    }

    override fun sendMessage(message: String) {
        for (player in participants) {
            player.sendMessage(message)
        }
    }

    override fun sendMessage(message: Component) {
        for (player in participants) {
            player.sendMessage(message)
        }
    }

    override fun sendMessage(user: ServerUser, message: String) {
        sendMessage(user.language.getMessage("global_server_chat", "%display_name%", user.displayNameAsString, "%message%", message))
    }

    override fun sendMessage(
        user: ServerUser,
        message: Component
    ) {
        sendMessage(
            user.language.getMessageComponent(
                "global_server_chat",
                "%display_name%", user.displayNameAsComponent,
                "%message%", message
            )
        )
    }
}