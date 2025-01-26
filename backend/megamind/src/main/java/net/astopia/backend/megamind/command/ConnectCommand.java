package net.astopia.backend.megamind.command;

import lombok.val;
import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.exception.CommandMessageException;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.api.backend.command.tabcomplete.Tabcomplete;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.megamind.manager.ServerManager;
import net.astopia.backend.megamind.server.AbstractPlayerHolder;

/**
 * @author Vihara
 */
@NamedCommand("connect")
public final class ConnectCommand {

    @Tabcomplete("[connectable]")
    @WithCondition("disableConsole")
    @WithCondition("group: admin")
    @WithCondition("minArgs: 1")
    @Executor
    public static void execute(final CoreExecutionSession session) throws CommandMessageException {
        val target = (AbstractPlayerHolder) ServerManager.INSTANCE.getPlayableServer(session.getArgument(0));
        if (target == null) {
            throw new CommandMessageException("Invalid target");
        }

        val gamer = session.getGamer();
        session.sendMessage(gamer.sendTo(target).name());
    }

}
