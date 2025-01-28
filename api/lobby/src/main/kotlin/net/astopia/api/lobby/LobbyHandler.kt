package net.astopia.api.lobby

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.minestom.server.command.CommandManager
import net.minestom.server.entity.Player
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.entity.EntityDamageEvent
import net.minestom.server.event.player.PlayerBlockBreakEvent
import net.minestom.server.event.player.PlayerBlockPlaceEvent
import net.minestom.server.event.player.PlayerMoveEvent
import net.minestom.server.event.player.PlayerSkinInitEvent
import net.minestom.server.potion.Potion
import net.minestom.server.potion.PotionEffect
import net.astopia.api.lobby.cmd.SpawnCommand
import net.astopia.api.lobby.ui.aui.StatsActionUi
import net.astopia.api.lobby.ui.gui.ServerSelectGui
import net.astopia.api.shared.ui.aui.ActionUi
import net.astopia.api.shared.user.ServerUser
import net.minestom.server.coordinate.Pos
import kotlin.properties.Delegates

object LobbyHandler {
    private val statsActionUi: StatsActionUi = StatsActionUi()
    private var voidY by Delegates.notNull<Int>()

    fun init(server: LobbyServer) {
        voidY = server.config.getInt("void_y")

        registerDefaultListeners(server.globalEventHandler)
        registerCommands(server.commandManager)
    }

    // TODO: implement rank conditional statements
    private fun registerDefaultListeners(globalEventHandler: GlobalEventHandler) {
        globalEventHandler
            .addListener(PlayerSkinInitEvent::class.java) { event ->
                ActionUi.display(event.player as ServerUser, statsActionUi)
                event.player.addEffect(Potion(
                    PotionEffect.fromNamespaceId("minecraft:hunger")!!,
                    1,
                    Potion.INFINITE_DURATION,
                ))
                ServerSelectGui(event.player as ServerUser)
            }
            .addListener(PlayerBlockBreakEvent::class.java) { event ->
                val player = event.player as ServerUser
                event.isCancelled = true
            }
            .addListener(PlayerBlockPlaceEvent::class.java) { event ->
                val player = event.player as ServerUser
                event.isCancelled = true
            }
            .addListener(EntityDamageEvent::class.java) { event ->
                val entity = event.entity

                if (entity is Player) {
                    event.isCancelled = true
                }
            }
            .addListener(PlayerMoveEvent::class.java) { event ->
                val newPos = event.newPosition
                if (newPos.y.toInt() == voidY) {
                    val player = event.player as ServerUser
                    val voidSound = Sound.sound(Key.key("entity.enderman.teleport"), Sound.Source.BLOCK, 1f, 1.2f)

                    player.teleport(player.respawnPoint)
                    player.playSound(voidSound, player.respawnPoint)
                    player.sendMessage(player.language.getMessageComponent("spawn_exit_attempt"))
                }
            }
    }

    private fun registerCommands(commandManager: CommandManager) {
        commandManager.register(SpawnCommand())
    }
}