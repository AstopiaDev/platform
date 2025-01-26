package net.astopia.backend.megamind.command;

import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.WithAlias;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.api.megamind.event.impl.gamer.PlayerVanishEvent;
import net.astopia.api.protocol.server.ServerScaleType;
import net.astopia.backend.base.command.exception.CommandTranslationException;
import net.astopia.backend.megamind.MegamindApp;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.megamind.data.gamer.Gamer;
import net.astopia.backend.megamind.server.AbstractScalable;

@WithAlias("v")
@NamedCommand("vanish")
public final class VanishCommand {

    @Executor
    @WithCondition("group: moderator")
    public static void execute(CoreExecutionSession session) throws CommandTranslationException {
        Gamer sender = session.getGamer();

        boolean vanished = !sender.isVanished();

        if (((AbstractScalable) sender.getCurrentServer()).getServerScale() != ServerScaleType.LOBBY && ((AbstractScalable) sender.getCurrentServer()).getServerScale() != ServerScaleType.PERSISTENT) {
            throw new CommandTranslationException("core_vanish_lobbyonly");
        }

        if (sender.setVanished(vanished)) {
            session.sendLocalizedMessage(vanished ? "core_vanish_enabled" : "core_vanish_disabled");
        }

        MegamindApp.INSTANCE.pluginManager().callEvent(new PlayerVanishEvent(sender, vanished));
    }
}
