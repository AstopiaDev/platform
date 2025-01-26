package net.astopia.backend.megamind.api.command;

import lombok.NonNull;
import net.astopia.api.backend.command.ICommand;
import net.astopia.api.backend.gamer.ICommandSender;
import net.astopia.api.backend.gamer.IGamer;
import net.astopia.backend.base.command.CommandManager;
import net.astopia.backend.base.command.execution.WrappedCommandExecutor;
import net.astopia.backend.megamind.MegamindApp;
import net.astopia.backend.megamind.data.gamer.Gamer;
import org.jetbrains.annotations.Nullable;

public class CoreCommandManager extends CommandManager {

    public CoreCommandManager() {
        super();
    }

    @Override
    public void registerCommand(final @NonNull String name,
                                final @NonNull ICommand command) {
        MegamindApp.INSTANCE.registerCommand(name, command);
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public CoreExecutionSession newCommandSession(final @NonNull WrappedCommandExecutor executor,
                                                  final @NonNull ICommandSender sender,
                                                  final @Nullable IGamer gamer,
                                                  final String @NonNull [] args) {
        return new CoreExecutionSession(executor, (CommandSender) sender, gamer == null ? null : (Gamer) gamer, args);
    }

}
