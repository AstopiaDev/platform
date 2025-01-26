package net.astopia.backend.megamind.command;

import lombok.val;
import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.api.backend.command.tabcomplete.Tabcomplete;
import net.astopia.backend.base.command.exception.CommandTranslationException;
import net.astopia.backend.base.util.color.ChatColor;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.megamind.manager.StaffChatManager;

/**
 * @author Vihara
 */
@NamedCommand("kick")
public final class KickCommand {

    @WithCondition("group: moderator")
    @WithCondition("minArgs: 1")
    @Tabcomplete("[playerName]")
    @Executor
    public static void execute(final CoreExecutionSession session) throws CommandTranslationException {
        val target = session.getGamer(0);
        var reason = "Not specified";
        if (session.getArgumentCount() > 1) {
            reason = session.getArgumentString(1);
        }

        target.disconnect(reason);

        StaffChatManager.INSTANCE.sendMessage(session.getDisplayName() + ChatColor.WHITE + " kicked " + target.getDisplayNameAsString() + ChatColor.WHITE + " for " + ChatColor.ITALIC + reason);
    }

}
