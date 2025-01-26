package net.astopia.backend.megamind.command;

import lombok.val;
import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.RelayExecutor;
import net.astopia.api.backend.command.RelayExecutorAlias;
import net.astopia.api.backend.command.WithAlias;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.exception.CommandException;
import net.astopia.api.backend.command.exception.CommandMessageException;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.api.backend.command.execution.SubcommandExecutor;
import net.astopia.api.backend.command.tabcomplete.Tabcomplete;
import net.astopia.api.backend.permission.Group;
import net.astopia.api.backend.setting.SettingType;
import net.astopia.api.backend.setting.impl.BooleanSetting;
import net.astopia.api.backend.setting.impl.EnumSetting;
import net.astopia.backend.base.command.exception.CommandTranslationException;
import net.astopia.backend.base.data.friends.FriendshipMeta;
import net.astopia.backend.base.util.color.ChatColor;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.megamind.data.friend.FriendManager;
import net.astopia.backend.megamind.data.gamer.Gamer;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This command is used for debug purposes.
 *
 * @author Vihara
 */
@WithAlias("бубен")
@WithAlias("buben")
@NamedCommand("debug")
public final class DebugCommand {

    @WithCondition("group: admin")
    
    @Executor
    public static void execute(final CoreExecutionSession session) {

        session.sendMessage("Invoked debug command. Args: " + Arrays.toString(session.getArguments()));
    }

    @SubcommandExecutor("dumpgamers")
    @WithCondition("group: admin")
    
    public static void dumpGamers(final CoreExecutionSession session) {
        session.sendMessage(Gamer.getGamers().stream().map(g -> g.getNameAsString() + "/" + g.getGamerId()).collect(Collectors.joining(", ")));
    }

    @SubcommandExecutor("unload")
    @WithCondition("group: admin")
    
    public static void unload(final CoreExecutionSession session) throws CommandTranslationException {
        val gamer = session.getGamer(0);
        gamer.quit();
        session.sendMessage("Unloaded " + gamer.getNameAsString());
    }

    @WithCondition("disableConsole")
    @WithCondition("group: admin")
    
    @SubcommandExecutor("ranks")
    public static void testRanks(final CoreExecutionSession session) {
        session.sendMessage(session.getGamer().getRanks().stream().map(ranks -> {
            return "RID: " + ranks.getId() + " - RL: " +
                    ranks.getLevel() + " - RGN: " + ranks.getGroup().getName() + ChatColor.WHITE +
                    " - RGL: " + ranks.getGroup().getLevel() + " - RAC: \n"
                    + Arrays.stream(Group.VALUES).map(g -> g.getName() + "=" + ranks.canApply(g)).collect(Collectors.joining("\n"));
        }).collect(Collectors.joining("\n------")));
    }

    @WithCondition("group: admin")
    @WithCondition("minArgs: 2")
    @Tabcomplete("[playerName] [property]")
    
    @SubcommandExecutor("prop")
    public static void getProperty(final CoreExecutionSession session) throws CommandMessageException {
        val target = session.getOfflineGamer(0, Gamer::loadProperties);
        val prop = session.getArgument(1);
        if (prop == null) {
            throw new CommandMessageException("dolbaeb");
        }

        session.sendMessage(prop + " -> " + target.getProperty(prop));
    }

    @WithCondition("group: admin")
    @WithCondition("minArgs: 2")
    @Tabcomplete("[playerName] [setting]")
    
    @SubcommandExecutor("setting-get")
    public static void getSetting(final CoreExecutionSession session) throws CommandMessageException {
        val target = session.getOfflineGamer(0, Gamer::loadProperties);
        val setting = SettingType.valueOf(session.getArgument(1), null);
        if (setting == null) {
            throw new CommandMessageException("dolbaeb");
        }

        session.sendMessage(setting.name() + " -> " + target.getSetting(setting).getValue());
    }

    @WithCondition("group: admin")
    
    @SubcommandExecutor("friends")
    public static void debugFriends(final CoreExecutionSession session) throws CommandException {
        if (session.getArgumentCount() > 0) {
            val option = session.getArgument(0);
            val one = session.getIntArgument(1, "first");
            val two = session.getIntArgument(2, "second");
            if (option.equals("create")) {
                session.sendMessage("C -> " + FriendManager.INSTANCE.sendFriendRequest(one, two));
                return;
            }

            if (option.equals("accept")) {
                session.sendMessage("A -> " + FriendManager.INSTANCE.acceptFriendRequest(one, two));
                return;
            }

            if (option.equals("deny")) {
                session.sendMessage("D -> " + FriendManager.INSTANCE.denyRequest(one, two));
                return;
            }

            if (option.equals("remove")) {
                session.sendMessage("R -> " + FriendManager.INSTANCE.removeFriend(one, two));
                return;
            }
        }
        session.sendMessage("-------------------------------");
        session.sendMessage("FriendsManager dump:");
        session.sendMessage("\n" + FriendManager.INSTANCE.getFriendshipMetas().stream().map(FriendshipMeta::toString).collect(Collectors.joining("\n")));
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    @WithCondition("group: admin")
    @WithCondition("minArgs: 3")
    @Tabcomplete("[playerName] [setting]")
    
    @SubcommandExecutor("setting-set")
    public static void setSetting(final CoreExecutionSession session) throws CommandMessageException {
        val target = session.getOfflineGamer(0, Gamer::loadProperties);
        val setting = SettingType.valueOf(session.getArgument(1), null);
        if (setting == null) {
            throw new CommandMessageException("dolbaeb");
        }

        if (setting.getSetting() instanceof BooleanSetting) {
            val settingInst = (BooleanSetting) session.getGamer().getSetting(setting);
            settingInst.setValue(Boolean.parseBoolean(session.getArgument(2)));

            target.setSetting(setting, settingInst);
        }

        if (setting.getSetting() instanceof EnumSetting<?>) {
            val enumSetting = (EnumSetting) session.getGamer().getSetting(setting);
            enumSetting.setValue(Enum.valueOf(enumSetting.getEnumClass(), session.getArgument(2)));

            target.setSetting(setting, enumSetting);
        }

        session.sendMessage("Updated: " + setting.name() + " -> " + target.getSetting(setting).getValue());
    }

    @RelayExecutorAlias({ "tf", "cock" })   // experimental
    @RelayExecutor("testframework")         // experimental
    @WithCondition("group: admin")
    @WithCondition("minArgs: 1")
    
    @Tabcomplete("[setting]")
    public static void executeFrameworkTest(final CoreExecutionSession session) {
        session.sendMessage("A -> " + Arrays.toString(session.getArguments()));
        session.sendMessage("W -> " + session.getExecutor().toString());
    }

/*    @WithCondition("group: admin")
    
    @SubcommandExecutor("party")
    public static void executePartyDebug(final CoreExecutionSession session) {
        session.sendMessage("----------------------------");
        if (!session.isConsole()) {
            session.sendMessage("Your party: " + session.getGamer().getParty());
        }
        session.sendMessage("PartyManager dump: ");
        for (val party : PartyManager.INSTANCE.getParties().int2ObjectEntrySet()) {
            session.sendMessage("#" + party.getIntKey() + " - " + party.getValue());
        }
    }*/
}
