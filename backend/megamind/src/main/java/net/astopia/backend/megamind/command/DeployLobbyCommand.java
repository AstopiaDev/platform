package net.astopia.backend.megamind.command;

import lombok.val;
import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.exception.CommandMessageException;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.api.protocol.server.GameType;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.megamind.manager.DeploymentManager;
import net.astopia.backend.megamind.manager.deployment.DeploymentResponse;
import net.astopia.backend.protocol.packet.play.ServerDeploy;
import net.astopia.backend.protocol.server.deploy.impl.BukkitDeployRequest;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Vihara
 */
@NamedCommand("deploylobby")
public final class DeployLobbyCommand {

    @WithCondition("group: admin")
    @WithCondition("minArgs: 1")
    @Executor
    public static void execute(CoreExecutionSession session) throws CommandMessageException {
        val gameType = GameType.valueOf(session.getArgument(0), null);
        if (gameType == null) {
            throw new CommandMessageException("Invalid game type specified! Use one of these: " + Arrays.stream(GameType.VALUES).map(GameType::name).collect(Collectors.joining(", ")));
        }

        DeploymentManager.INSTANCE.sendDeploymentRequest(new BukkitDeployRequest(gameType.bakeLobbyData()),
                ServerDeploy.DeploymentTarget.BUKKIT, false, null, -1,
                (result) -> {
                    if (result == DeploymentResponse.DENIED_BY_NODE
                            || result == DeploymentResponse.DEPLOYMENT_TIMEOUT
                            || result == DeploymentResponse.TRANSACTION_FAILED) {
                        session.sendMessage("Transaction failed! (" + result.name() + ") / (#" + result.transactionId() + ")");
                    } else {
                        session.sendMessage("Queued deployment of lobby for {}!", gameType.getHumanName());
                    }
                });
    }

}
