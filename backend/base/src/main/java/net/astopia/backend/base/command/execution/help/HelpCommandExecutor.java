package net.astopia.backend.base.command.execution.help;

import lombok.NonNull;
import net.astopia.backend.base.command.CommandParams;
import net.astopia.backend.base.command.CommandWrapper;
import net.astopia.backend.base.command.execution.WrappedCommandExecutor;

public class HelpCommandExecutor extends WrappedCommandExecutor {
    private static final int HASH = "help".hashCode();

    public HelpCommandExecutor(final @NonNull CommandWrapper wrapper) {
        super(new CommandParams(wrapper, HASH, session -> HelpPrinter.print(wrapper, session)));
    }
}
