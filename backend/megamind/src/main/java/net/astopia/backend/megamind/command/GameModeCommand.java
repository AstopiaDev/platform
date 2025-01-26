package net.astopia.backend.megamind.command;

import lombok.val;
import net.astopia.api.protocol.type.GameMode;
import net.astopia.backend.base.command.CommandManager;
import net.astopia.backend.base.command.DynamicCommand;
import net.astopia.backend.base.command.execution.ExecutionSession;
import net.astopia.backend.base.util.color.ChatColor;
import net.astopia.backend.megamind.data.gamer.Gamer;
import net.astopia.backend.megamind.manager.ServerManager;
import net.astopia.backend.megamind.server.AbstractPlayerHolder;

public class GameModeCommand extends DynamicCommand {
    GameMode gameMode;
    String name;

    public GameModeCommand(CommandManager manager, String name, GameMode gameMode) {
        super(manager, name, null);
        this.name = name;
        this.gameMode = gameMode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    protected <T extends ExecutionSession> void execute(T session) {
        Gamer gamer = (Gamer) session.getSender();

        val target = (AbstractPlayerHolder) ServerManager.INSTANCE.getPersistentServer(gamer, gameMode);

        if (target == null) {
            gamer.sendMessageAsString(ChatColor.RED + "Could not find you a server!");
            return;
        }

        gamer.sendMessageAsString(ChatColor.GREEN + "Sending you to " + gameMode.getHumanName() + "!");
        gamer.sendMessageAsString(gamer.sendTo(target).name());
    }
}
