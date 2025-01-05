package net.skykingdoms.api.shared.user

import net.minestom.server.entity.Player
import net.minestom.server.network.PlayerProvider
import net.minestom.server.network.player.GameProfile
import net.minestom.server.network.player.PlayerConnection

class ServerUserProvider() : PlayerProvider {
    override fun createPlayer(
        connection: PlayerConnection,
        gameProfile: GameProfile
    ): Player {
        return ServerUser(connection, gameProfile)
    }
}