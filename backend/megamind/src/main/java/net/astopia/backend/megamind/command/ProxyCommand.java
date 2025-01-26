package net.astopia.backend.megamind.command;

import lombok.NonNull;
import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;

/**
 * @author Vihara
 */
@NamedCommand("proxy")
public final class ProxyCommand {

    @WithCondition("disableConsole")
    @Executor
    public static void execute(final @NonNull CoreExecutionSession session) {
        session.sendMessage(session.getGamer().getProxyServer().getName());
    }

}
