package net.astopia.backend.megamind.command;

import lombok.NonNull;
import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.WithAlias;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.backend.base.util.color.ChatColor;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.megamind.data.gamer.Gamer;

/**
 * @author Vihara
 */
@WithAlias("networkbroadcast")
@NamedCommand("alert")
public final class AlertCommand {
    @WithCondition("minArgs: 1")
    @WithCondition("group: admin")
    @Executor
    public static void execute(final @NonNull CoreExecutionSession session) {
        Gamer.getGamers().forEach(gamer -> {
            gamer.sendMessageAsString(
                    "\n" +
                    ChatColor.RED + ChatColor.BOLD + "Alert: " + ChatColor.WHITE + session.getArgumentString(0) +
                    "\n");
        });
    }

}
