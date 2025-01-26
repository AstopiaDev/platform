package net.astopia.backend.megamind.command;

import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.backend.megamind.MegamindApp;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;

@NamedCommand("shutdown")
public class ShutdownCommand {
    @Executor
    @WithCondition("consoleOnly")
    public static void execute(CoreExecutionSession session) {
        MegamindApp.INSTANCE.shutdown();
    }
}
