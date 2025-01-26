package net.astopia.api.shared.skin

import net.minestom.server.entity.PlayerSkin
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.PlayerSkinInitEvent
import net.astopia.api.shared.user.ServerUser
import kotlin.random.Random

object PlayerSkinHandler {
    private lateinit var defaultSkins: Array<PlayerSkin>

    fun init(globalEventHandler: GlobalEventHandler) {
        defaultSkins = arrayOf(
            PlayerSkin("", "")
        )
        globalEventHandler.addListener(PlayerSkinInitEvent::class.java) { event ->
            val player = event.player as ServerUser
            if (player.isPremium) {
                event.skin = PlayerSkin.fromUuid(player.uuid.toString())
            } else {
                event.skin = getRandomSkin()
            }
        }
    }

    fun getDefaultSkin(index: Int): PlayerSkin? {
        return if (::defaultSkins.isInitialized && index in defaultSkins.indices) {
            defaultSkins[index]
        } else {
            null
        }
    }

    fun getAllDefaultSkins(): Array<PlayerSkin>? {
        return if (::defaultSkins.isInitialized) defaultSkins else null
    }

    fun getRandomSkin(): PlayerSkin? {
        return if (::defaultSkins.isInitialized && defaultSkins.isNotEmpty()) {
            defaultSkins[Random.nextInt(defaultSkins.size)]
        } else {
            null
        }
    }
}