package net.astopia.backend.megamind.command.auth;

import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.WithAlias;
import net.astopia.api.backend.command.WithMultipleAliases;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.megamind.data.auth.AuthManager;
import net.astopia.backend.megamind.data.gamer.Gamer;

@WithMultipleAliases(
        {
                @WithAlias("l"),
                @WithAlias("signin")
        }
)
@NamedCommand("login")
public class LoginCommand {
    @Executor
    @WithCondition("noAuth")
    @WithCondition("minArgs: 1")
    public static void execute(CoreExecutionSession session) {
        Gamer gamer = session.getGamer();
        String password = session.getArgument(0);

        if (gamer.isLoggedIn()) {
            gamer.sendLocalizedMessage("authentication_already_logged_in");
            return;
        }

        if (!gamer.isRegistered()) {
            gamer.sendLocalizedMessage("authentication_not_registered");
            return;
        }

        if (AuthManager.INSTANCE.loginGamer(gamer, password)) {
            gamer.setLoggedIn(true);
            gamer.sendLocalizedMessage("authentication_logged_in");
        } else {
            gamer.sendLocalizedMessage("authentication_password_incorrect");
        }
    }
}
