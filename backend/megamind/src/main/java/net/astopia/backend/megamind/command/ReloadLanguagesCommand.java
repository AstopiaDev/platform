package net.astopia.backend.megamind.command;

import lombok.val;
import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.backend.base.dependency.Dependency;
import net.astopia.backend.base.language.Language;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.megamind.manager.ProxyManager;
import net.astopia.backend.megamind.manager.ServerManager;
import net.astopia.backend.protocol.packet.play.S15ReloadLanguages;

import java.util.Arrays;

/**
 * @author Vihara
 */
@NamedCommand("reloadlanguages")
public final class ReloadLanguagesCommand {

    @Dependency
    private static ServerManager manager;

    @Executor
    @WithCondition("group: admin")
    public static void executor(CoreExecutionSession session) {
        boolean updateAll = Arrays.asList(session.getArguments()).contains("@all");

        val packet = new S15ReloadLanguages();

        if (updateAll) {
            Language.reloadLanguages();

            // Sending shit to ALL proxies
            ProxyManager.INSTANCE.sendToProxies(packet);

            // Sending shit to ALL bukkit instances (that includes mini/mega/lobby instances)
            ServerManager.INSTANCE.sendToServers(packet);

            session.sendPrefixedMessage("Translations updated networkwide :D");
        } else {
            if (session.isConsole()) {
                Language.reloadLanguages();

                session.sendPrefixedMessage("Translations updated locally UwU");
            } else {
                session.getGamer().sendToServer(packet);
                session.sendPrefixedMessage("Translations updated at " + session.getGamer().getBukkitServer().getName());
            }
        }
    }

}
