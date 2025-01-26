package net.astopia.backend.megamind.command;

import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.WithAlias;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.megamind.manager.ServerManager;
import net.astopia.backend.megamind.server.AbstractScalable;

@WithAlias("strezniki")
@NamedCommand("allservers")
public class AllServersCommand {

    @Executor
    @WithCondition("group: owner")
    public static void execute(CoreExecutionSession session) {
        StringBuilder builder = new StringBuilder();
        builder.append("All servers: \n");
        for (AbstractScalable scalable : ServerManager.INSTANCE.getServers()) {
            builder.append(scalable.getName()).append(", ");
        }

        session.sendMessage(builder.toString());
    }
}
