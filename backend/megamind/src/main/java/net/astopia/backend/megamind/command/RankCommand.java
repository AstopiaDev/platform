package net.astopia.backend.megamind.command;

import lombok.val;
import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.api.backend.command.execution.SubcommandExecutor;
import net.astopia.api.backend.command.tabcomplete.Tabcomplete;
import net.astopia.api.backend.permission.Group;
import net.astopia.backend.base.command.exception.CommandTranslationException;
import net.astopia.backend.base.gamer.CommonGamer;
import net.astopia.backend.base.permission.UserRanks;
import net.astopia.backend.base.util.color.ChatColor;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;

@NamedCommand("rank")
public class RankCommand {
    @Executor
    @WithCondition("group: owner")
    public static void execute(CoreExecutionSession session) {
        session.sendMessage("Not enough args");
    }

    // rank set Vihara owner
    @WithCondition("group: owner")
    @WithCondition("minArgs: 2")
    @WithCondition("antiDolbaeb")
    @Tabcomplete("[playerName] [rank]")
    @SubcommandExecutor("set")
    public static void setExecute(CoreExecutionSession session) throws CommandTranslationException {
        val target = session.getOfflineGamer(0, CommonGamer::loadGroupsAndName);
        val rank = Group.fromLegacyGroup(session.getArgument(1));

        target.setRank(rank, null);
        session.sendMessage("§aSet {}'s rank to {}", target.getNameAsString(), rank.getDisplayName());
    }

    // rank add Vihara owner
    @WithCondition("group: owner")
    @WithCondition("minArgs: 2")
    @WithCondition("antiDolbaeb")
    @Tabcomplete("[playerName] [rank]")
    @SubcommandExecutor("add")
    public static void addExecute(CoreExecutionSession session) throws CommandTranslationException {
        val target = session.getOfflineGamer(0, CommonGamer::loadGroupsAndName);
        val rank = Group.fromLegacyGroup(session.getArgument(1));

        target.addRank(rank, null);
        session.sendMessage("§aAdded {} §arank to {}", rank.getDisplayName(), target.getNameAsString());
    }

    // rank remove Vihara owner
    @WithCondition("group: owner")
    @WithCondition("minArgs: 2")
    @WithCondition("antiDolbaeb")
    @Tabcomplete("[playerName] [rank]")
    @SubcommandExecutor("remove")
    public static void removeExecute(CoreExecutionSession session) throws CommandTranslationException {
        val target = session.getOfflineGamer(0, CommonGamer::loadGroupsAndName);
        val rank = Group.fromLegacyGroup(session.getArgument(1));


        val cock = (UserRanks) target.getRanks();
        if (!cock.canApply(rank)) {
            session.sendMessage("{}" + ChatColor.GRAY + " didn't have {} in their ranks!", target.getNameAsString(), rank.getDisplayName());
            return;
        }

        target.removeRank(cock.getAllRanks().stream().filter(r -> r.getGroup() == rank).findFirst().orElse(null));
        session.sendMessage("§aRemoved {} §arank from {}", rank.getDisplayName(), target.getNameAsString());
    }
}
