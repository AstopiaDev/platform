package net.astopia.backend.megamind.command;

import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.api.backend.command.tabcomplete.Tabcomplete;
import net.astopia.api.protocol.platform.Platform;
import net.astopia.backend.megamind.MegamindApp;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;

@NamedCommand("reloadmind")
public class MindReloadCommand {


    @Executor
    @WithCondition("group: owner")
    @WithCondition("antiDolbaeb")
    @Tabcomplete("[pluginName]")
    public static void execute(CoreExecutionSession session) {
        if (session.getArgumentCount() == 1) {
            String pluginName = session.getArgument(0);
            Platform.logger.info("Reloading " + pluginName + "...");
            MegamindApp.INSTANCE.pluginManager().reloadPlugin(pluginName);
            Platform.logger.info("Reloaded " + pluginName + "...");
            return;
        }

        Platform.logger.info("Reloading all plugins...");
        MegamindApp.INSTANCE.pluginManager().reloadPlugins(MegamindApp.INSTANCE.plugins());
        Platform.logger.info("All plugins have been reloaded.");
    }
}
