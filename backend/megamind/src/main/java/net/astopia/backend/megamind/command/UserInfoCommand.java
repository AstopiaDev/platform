package net.astopia.backend.megamind.command;

import lombok.NonNull;
import lombok.val;
import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.api.backend.command.tabcomplete.Tabcomplete;
import net.astopia.api.backend.permission.Group;
import net.astopia.api.backend.permission.PermissionType;
import net.astopia.backend.base.command.exception.CommandTranslationException;
import net.astopia.backend.base.util.color.ChatColor;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.megamind.data.gamer.Gamer;

/**
 * @author Vihara
 */
@NamedCommand("userinfo")
public final class UserInfoCommand {

    @Tabcomplete("[playerName]")
    @WithCondition("group: owner")
    @WithCondition("minArgs: 1")
    @Executor
    public static void execute(final @NonNull CoreExecutionSession session) throws CommandTranslationException {
        val gamer = session.getOfflineGamer(0, Gamer::load);
        session.sendLocalizedMessage("core_userinfo",
                "%username%", gamer.getDisplayNameAsString(),
                "%uuid%", gamer.getPlayerUniqueId(),
                "%domain%", gamer.isOnline() ? gamer.getDomain() : ChatColor.RED + "offline",
                "%address%", gamer.isOnline() ? gamer.getAddress().getHostAddress() : ChatColor.RED + "offline",
                "%rank%", gamer.getFormattedRank(gamer.getRanks().getPriorityRank().getGroup(), false) + ChatColor.WHITE + (gamer.getRanks().getPriorityRank(PermissionType.DEFAULT).getGroup() == Group.DEFAULT ? "" : " (" + gamer.getFormattedRank(gamer.getRanks().getPriorityRank(PermissionType.DEFAULT).getGroup(), false) + ChatColor.WHITE + ")"),
                "%network%", gamer.isOnline() ? gamer.getProxyServer().getName() : ChatColor.RED + "offline",
                "%server%", gamer.isOnline() ? gamer.getCurrentServer() != null ? gamer.getCurrentServer().getName() : "ERR" : ChatColor.RED + "offline",
                "%firstLogin%", gamer.getFirstLogin());
    }

}
