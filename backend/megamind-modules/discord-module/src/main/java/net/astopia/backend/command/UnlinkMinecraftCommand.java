package net.astopia.backend.command;

import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.megamind.data.gamer.Gamer;

@NamedCommand("unlink")
public class UnlinkMinecraftCommand {
    @Executor
    public static void execute(CoreExecutionSession session) {
        Gamer gamer = session.getGamer();
        long discordId = gamer.getPropertyOrDefault("discord_id", (long) 0);

        if (discordId == (long) 0 || discordId == (long) -1) {
            gamer.sendMessageAsString("§cYou are not linked to a Minecraft account.");
            return;
        }

        gamer.setProperty("discord_id", (long) 0);
        gamer.sendMessageAsString("§aYou have successfully unlinked your Minecraft account!");
    }
}
