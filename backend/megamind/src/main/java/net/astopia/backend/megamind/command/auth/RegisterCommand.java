package net.astopia.backend.megamind.command.auth;

import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.WithAlias;
import net.astopia.api.backend.command.WithMultipleAliases;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.backend.base.util.hash.HashType;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.megamind.data.auth.AuthManager;
import net.astopia.backend.megamind.data.gamer.Gamer;

@WithMultipleAliases(
        {
                @WithAlias("reg"),
                @WithAlias("signup")
        }
)
@NamedCommand("register")
public class RegisterCommand {
    @Executor
    @WithCondition("noAuth")
    @WithCondition("minArgs: 2")
    public static void execute(CoreExecutionSession session) {
        Gamer gamer = session.getGamer();
        String password = session.getArgument(0);
        String confirmPassword = session.getArgument(1);

        if (gamer.isRegistered()) {
            gamer.sendLocalizedMessage("authentication_already_registered");
            return;
        }

        if (!password.equals(confirmPassword)) {
            gamer.sendLocalizedMessage("authentication_password_no_match");
            return;
        }

        AuthManager.INSTANCE.registerGamer(gamer, password);
        gamer.sendLocalizedMessage("authentication_registered");
    }
}
