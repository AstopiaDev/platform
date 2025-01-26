package net.astopia.backend.megamind.command;

import lombok.val;
import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.api.backend.command.tabcomplete.Tabcomplete;
import net.astopia.backend.base.command.exception.CommandTranslationException;
import net.astopia.backend.base.util.color.ChatColor;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;

/**
 * @author Vihara
 */
@NamedCommand("port")
public final class PortCommand {

    @WithCondition("disableConsole")
    @WithCondition("group: admin")
    @Tabcomplete("[playerName]")
    @WithCondition("minArgs: 1")
    @Executor
    public static void execute(CoreExecutionSession session) throws CommandTranslationException {
        val target = session.getGamer(0);
        if (target.getCurrentServer() == session.getGamer().getCurrentServer()) {
            session.sendMessage(ChatColor.RED + "This player is already connected to your server!");
            return;
        }

        target.sendTo(session.getGamer().getCurrentServer());

        session.sendMessage("Ported " + target.getDisplayNameAsString());
    }

}
