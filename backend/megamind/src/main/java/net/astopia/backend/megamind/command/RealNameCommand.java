package net.astopia.backend.megamind.command;

import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.backend.base.util.color.ChatColor;
import net.astopia.backend.megamind.AsyncExecution;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.megamind.data.gamer.Gamer;

import java.util.List;
import java.util.stream.Collectors;

@NamedCommand("realname")
public class RealNameCommand {

    @Executor
    @WithCondition("minArgs: 1")
    @WithCondition("group: admin")
    public static void execute(CoreExecutionSession session) {
        String targetNickname = session.getArgument(0).toLowerCase();

        AsyncExecution.INSTANCE.run(() -> {
            List<Gamer> matchingGamers = Gamer.getGamers().parallelStream()
                    .filter(gamer -> {
                        String gamerNickName = gamer.getProperty("nickname");
                        if (gamerNickName != null) {
                            String strippedNickName = ChatColor.stripColor(gamerNickName).toLowerCase();
                            return strippedNickName.contains(targetNickname);
                        }
                        return false;
                    })
                    .collect(Collectors.toList());

            StringBuilder message = new StringBuilder();

            if (matchingGamers.isEmpty()) {
                message.append("§cCan't find out who goes by ").append(targetNickname);
            } else if (matchingGamers.size() == 1) {
                message.append("§eYou're looking for ").append(matchingGamers.get(0).getColoredName())
                        .append(" §e(§r" + matchingGamers.get(0).getProperty("nickname") + "§e)")
                        .append("§e.");
            } else if (matchingGamers.size() == 2) {
                message.append("§eYou're looking for ")
                        .append(matchingGamers.get(0).getColoredName())
                        .append(" §e(§r" + matchingGamers.get(0).getProperty("nickname") + "§e)")
                        .append("§e or ")
                        .append(matchingGamers.get(1).getColoredName())
                        .append(" §e(§r" + matchingGamers.get(1).getProperty("nickname") + "§e)")
                        .append("§e.");
            } else {
                message.append("§eYou're looking for:\n");
                matchingGamers.forEach(gamer -> message.append(" §e- ").append(gamer.getColoredName()).append(" §e(§r" + gamer.getProperty("nickname") + "§e)").append("\n"));
            }

            session.sendMessage(message.toString().trim());
        });
    }
}
