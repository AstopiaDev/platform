package net.astopia.backend.megamind.command;

import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.WithAlias;
import net.astopia.api.backend.command.WithMultipleAliases;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.megamind.data.gamer.Gamer;

@WithMultipleAliases({
        @WithAlias("search"),
        @WithAlias("find"),
        @WithAlias("stalk"),
        @WithAlias("whereareyou")
})
@NamedCommand("find")
public class FindCommand {

    @Executor
    @WithCondition("group: admin")
    @WithCondition("minArgs: 1")
    public static void execute(CoreExecutionSession session) {
        String username = session.getArgument(0);
        Gamer gamer = Gamer.getGamer(username);

        if (gamer == null) {
            session.sendMessage("Gamer not found: " + username);
            return;
        }

        if (gamer.getCurrentServer() == null) {
            session.sendMessage("§5Beep, boop, beep! ~~~\n§8" + gamer.getNameAsString() + " is currently nowhere to be found.");
            return;
        }


        session.sendMessage("§5Beep, boop, beep! ~~~\n§8" + gamer.getNameAsString() + " is currently on server: " + gamer.getCurrentServer().getName());
    }
}
