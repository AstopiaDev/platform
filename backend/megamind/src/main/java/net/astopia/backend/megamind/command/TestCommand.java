package net.astopia.backend.megamind.command;

import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.api.megamind.event.impl.gamer.GamerJoinEvent;
import net.astopia.backend.megamind.MegamindApp;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;

@NamedCommand("testevent")
public class TestCommand {
    @Executor
    public static void execute(CoreExecutionSession session) {
        session.sendMessage("Sending test event");
        MegamindApp.INSTANCE.pluginManager().callEvent(new GamerJoinEvent(session.getGamer()));
    }
}
