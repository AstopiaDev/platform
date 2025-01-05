package net.skykingdoms.api.shared.user

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.network.player.GameProfile
import net.minestom.server.network.player.PlayerConnection
import net.skykingdoms.api.backend.gamer.IGamer
import net.skykingdoms.api.backend.language.ILanguage
import net.skykingdoms.api.backend.permission.IUserRank
import net.skykingdoms.api.backend.permission.UserRankContainer
import net.skykingdoms.api.shared.chat.Chat
import net.skykingdoms.api.shared.chat.ServerChat
import net.skykingdoms.backend.base.language.Language
import java.net.InetAddress
import java.net.InetSocketAddress
import java.util.UUID
import java.util.concurrent.CompletableFuture

class ServerUser(val connection: PlayerConnection,
                 gameProfile: GameProfile,
                 var chat: Chat = ServerChat.defaultChat,
                 private var language: ILanguage = Language.DEFAULT
) : Player(connection, gameProfile), IGamer {
    private val id: Int = -1

    override fun UNSAFE_init(): CompletableFuture<Void?>? {
        chat.participants().add(this)
        return super.UNSAFE_init()
    }

    override fun sendMessageAsString(message: String?) {
        if (message != null) {
            sendMessage(Component.text(message))
        }
    }

    override fun dispatchCommand(command: String?) {

    }

    override fun isConsole(): Boolean {
        return false
    }

    override fun getGamerId(): Int {
        return id
    }

    override fun getPlayerUniqueId(): UUID {
        return uuid
    }

    override fun getNameAsString(): String {
        return username
    }

    override fun getNameAsComponent(): Component {
        return Component.text(nameAsString)
    }

    override fun getDisplayNameAsString(): String {
        return username
    }

    override fun getDisplayNameAsComponent(): Component {
        return Component.text(displayNameAsString)
    }

    override fun getLanguage(): ILanguage {
        return language
    }

    override fun getRanks(): UserRankContainer<out IUserRank?>? {
        TODO("Not yet implemented")
    }

    override fun getAddress(): InetAddress? {
        val remoteAddress = playerConnection.remoteAddress
        if (remoteAddress is InetSocketAddress) {
            return remoteAddress.address
        }
        return null
    }

    override fun getProtocol(): Int {
        return connection.protocolVersion
    }

    override fun isPremium(): Boolean {
        return true
    }

    override fun isLoggedIn(): Boolean {
        return false
    }
}