package net.astopia.backend.megamind.command;

import lombok.val;
import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.exception.CommandMessageException;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.backend.base.data.state.NetworkState;
import net.astopia.backend.base.util.color.ChatColor;
import net.astopia.backend.megamind.MegamindApp;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.megamind.data.gamer.Gamer;
import net.astopia.backend.megamind.manager.ProxyManager;
import net.astopia.backend.megamind.manager.StaffChatManager;
import net.astopia.backend.protocol.packet.play.S28UpdateNetworkState;

/**
 * @author Vihara
 */
@NamedCommand("networkstate")
public final class NetworkStateCommand {

    @WithCondition("consoleOnly") // not to kick all niggas accidentally
    @Executor
    public static void execute(CoreExecutionSession session) throws CommandMessageException {
        if (session.getArgumentCount() == 0) {
            session.sendMessage("Current network state: " + MegamindApp.INSTANCE.networkState().name());
            return;
        }

        val state = NetworkState.getByName(session.getArgument(0));
        if (state == null) {
            throw new CommandMessageException("Invalid network state!");
        }

        if (MegamindApp.INSTANCE.networkState() == state) {
            throw new CommandMessageException("The network is already in " + state.name() + "-state");
        }

        // sending shit to proxies
        ProxyManager.INSTANCE.sendToProxies(new S28UpdateNetworkState(state.ordinal()));

        // updating state on coordinator
        MegamindApp.INSTANCE.networkState(state);

        // calling consumer
        Gamer.getGamers().forEach($ -> state.getOnJoin().accept($));

        StaffChatManager.INSTANCE.sendMessage(ChatColor.WHITE + "Updated network state to " + ChatColor.YELLOW + state.name());
    }

}
