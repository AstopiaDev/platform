package net.astopia.backend.megamind.command;

import lombok.val;
import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.megamind.manager.ServerManager;

@NamedCommand("removebukkitserver")
public class RemoveBukkitServerCommand {
    @Executor
    @WithCondition("group: owner")
    @WithCondition("minArgs: 1")
    public static void execute(CoreExecutionSession session) {
        String name = session.getArgument(0);
        var force = false;

        for (val arg : session.getArguments()) {
            if (arg.equalsIgnoreCase("-f")) force = true;
        }

        ServerManager.INSTANCE.removeBukkitServer(name, true, force);
        session.sendMessage("Removing bukkit server: {} - Flags: F({})", name, force);
    }
}
