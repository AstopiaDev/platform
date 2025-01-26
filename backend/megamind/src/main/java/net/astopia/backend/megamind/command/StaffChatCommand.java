package net.astopia.backend.megamind.command;

import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.WithAlias;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.backend.base.util.color.ChatColor;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.megamind.manager.StaffChatManager;

/**
 * @author Vihara
 */
@WithAlias("sc")
@NamedCommand("staffchat")
public final class StaffChatCommand {

    @WithCondition("group: jr_helper")
    @WithCondition("minArgs: 1")
    @Executor
    public static void execute(final CoreExecutionSession session) {
        StaffChatManager.INSTANCE.sendMessage(session.getGamer().getColoredName() + ChatColor.WHITE + ": " + session.getArgumentString(0));
    }

}
