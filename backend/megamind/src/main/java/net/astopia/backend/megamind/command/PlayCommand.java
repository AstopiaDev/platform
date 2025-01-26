package net.astopia.backend.megamind.command;

import lombok.NonNull;
import lombok.val;
import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.exception.CommandMessageException;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.api.backend.command.tabcomplete.Tabcomplete;
import net.astopia.api.protocol.type.GameMode;
import net.astopia.backend.base.util.color.ChatColor;
import net.astopia.backend.base.util.optionals.Optional;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.megamind.manager.ServerManager;
import net.astopia.backend.megamind.matchmaking.MatchmakingManager;
import net.astopia.backend.megamind.server.impl.GameServer;

/**
 * @author Vihara
 */
@NamedCommand("play")
public final class PlayCommand {

    @Tabcomplete("[gameMode]")
    @WithCondition("minArgs: 1")
    @Executor
    public static void execute(final @NonNull CoreExecutionSession session) throws CommandMessageException {
        val gameMode = GameMode.valueOf(session.getArgument(0), null);
        val gamer = session.getGamer();
        if (gameMode == null) {
            throw new CommandMessageException(ChatColor.RED + "Invalid game mode!");
        }

        Optional<GameServer> optional;
        if (gameMode == GameMode.RANKED_NORMAL) {
            optional = MatchmakingManager.INSTANCE.getOptimalServer(gamer);
        } else {
            optional = Optional.ofNullable((GameServer) ServerManager.INSTANCE.getBestByOnline(session.getGamer(), gameMode.bakeData(), true));
        }

        if (optional.isEmpty()) {
            throw new CommandMessageException(ChatColor.RED + "Couldn't find a server for you :(");
        }

        gamer.sendTo(optional.get());
    }

}
