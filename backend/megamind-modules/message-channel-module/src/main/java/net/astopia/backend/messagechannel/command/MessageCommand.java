package net.astopia.backend.messagechannel.command;

import lombok.val;
import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.WithAlias;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.api.backend.command.tabcomplete.Tabcomplete;
import net.astopia.backend.base.command.exception.CommandTranslationException;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.messagechannel.manager.MessageManager;

@WithAlias("w")
@WithAlias("whisper")
@WithAlias("msg")
@WithAlias("m")
@WithAlias("tell")
@WithAlias("text")
@WithAlias("t")
@WithAlias("pm")
@NamedCommand("message")
public final class MessageCommand {

    @Executor
    @Tabcomplete("[playerName]")
    @WithCondition("minArgs: 2")
    public static void execute(CoreExecutionSession session) throws CommandTranslationException {
        val sender = session.getGamer();
        val receiver = session.getGamer(0);
/*        if (!receiver.getRanks().canApply(Group.HELPER) && sender.isVanished()) {
            throw new CommandTranslationException("function_disabled_vanish");
        }*/

        if (receiver == sender) {
            throw new CommandTranslationException("message_self");
        }

        MessageManager.INSTANCE.processMessage(sender, receiver, session.getArgumentString(1));
    }

}
