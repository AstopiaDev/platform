package net.astopia.backend.megamind.data.punishment.command.impl;

import lombok.val;
import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.api.backend.punishment.PunishmentType;
import net.astopia.backend.base.command.exception.CommandTranslationException;
import net.astopia.backend.base.gamer.CommonGamer;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.megamind.data.punishment.command.PunishmentCommand;

/**
 * Main mute command
 * @author Vihara
 */
@NamedCommand("mute")
public final class MuteCommand extends PunishmentCommand {

    @WithCondition("group: moderator")
    @WithCondition("minArgs: 2")
    @Executor
    public void execute(final CoreExecutionSession session) throws CommandTranslationException {
        val target = session.getOfflineGamer(0, CommonGamer::loadGroups);

        executePunishment(
                target,
                session.getSender(),
                session.getArgumentString(2),
                parseDuration(session.getArgument(1)),
                PunishmentType.MUTE);
    }

}
