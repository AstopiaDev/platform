package net.skykingdoms.backend.megamind.command;

import net.skykingdoms.api.backend.command.NamedCommand;
import net.skykingdoms.api.backend.command.condition.WithCondition;
import net.skykingdoms.api.backend.command.execution.Executor;
import net.skykingdoms.api.backend.command.tabcomplete.Tabcomplete;
import net.skykingdoms.api.protocol.platform.Platform;
import net.skykingdoms.backend.megamind.MegamindApp;
import net.skykingdoms.backend.megamind.api.command.CoreExecutionSession;

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
