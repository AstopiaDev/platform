package cmd

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
import net.skykingdoms.api.shared.user.ServerUser

class SpawnCommand : Command("spawn") {
    init {
        setDefaultExecutor { sender, _ ->
            if (sender !is Player) {
                sender.sendMessage("Only players can execute this command.")
                return@setDefaultExecutor
            }

            val serverUser = sender as? ServerUser
            if (serverUser == null) {
                sender.sendMessage("Unable to retrieve user data.")
                return@setDefaultExecutor
            }

            val teleportSound = Sound.sound(Key.key("entity.enderman.teleport"), Sound.Source.BLOCK, 1f, 1.2f)
            serverUser.teleport(serverUser.respawnPoint)
            serverUser.playSound(teleportSound, serverUser.respawnPoint)
            serverUser.sendMessage(serverUser.language.getMessageComponent("spawn_command_success"))
        }
    }
}