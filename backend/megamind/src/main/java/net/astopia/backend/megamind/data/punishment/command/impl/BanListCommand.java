package net.astopia.backend.megamind.data.punishment.command.impl;


import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.WithAlias;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.exception.CommandException;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.api.backend.punishment.PunishmentType;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;
import net.astopia.backend.megamind.data.punishment.command.PunishmentListCommand;

/**
 * Lists all ban records
 * @author Vihara
 */
@WithAlias("bans")
@NamedCommand("banlist")
public final class BanListCommand extends PunishmentListCommand {

    @WithCondition("group: moderator")
    @WithCondition("minArgs: 1")
    @Executor
    public void execute(final CoreExecutionSession session) throws CommandException {
        sendPunishmentsList(session, PunishmentType.BAN);
    }

}
