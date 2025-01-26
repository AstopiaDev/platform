package net.astopia.backend.megamind.command;

import lombok.NonNull;
import lombok.val;
import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.api.backend.command.tabcomplete.Tabcomplete;
import net.astopia.api.protocol.server.GameType;
import net.astopia.api.protocol.server.ServerScaleType;
import net.astopia.api.protocol.server.impl.ScalableDataImpl;
import net.astopia.api.protocol.type.RemoteConnectedType;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.megamind.manager.DeploymentManager;
import net.astopia.backend.protocol.packet.play.ServerDeploy;
import net.astopia.backend.protocol.server.deploy.impl.BukkitDeployRequest;
import net.astopia.backend.protocol.server.deploy.impl.ProxyDeployRequest;

@NamedCommand("deploytest")
public final class DeployTestCommand {

    @Tabcomplete("[remoteType]")
    @WithCondition("minArgs: 1")
    @Executor
    public static void execute(final @NonNull CoreExecutionSession session) {
        val cock = RemoteConnectedType.valueOf(session.getArgument(0));
        DeploymentManager.INSTANCE.sendDeploymentRequest(cock == RemoteConnectedType.PROXY
                        ? new ProxyDeployRequest() : new BukkitDeployRequest(new ScalableDataImpl(ServerScaleType.LOBBY, GameType.MAIN, null)),
                cock == RemoteConnectedType.PROXY ? ServerDeploy.DeploymentTarget.PROXY : ServerDeploy.DeploymentTarget.BUKKIT, false, null, 0, response -> {
                    session.sendMessage("Result: " + response);
                });
    }

}
