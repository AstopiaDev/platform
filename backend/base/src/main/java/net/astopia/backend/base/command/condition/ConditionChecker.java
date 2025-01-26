package net.astopia.backend.base.command.condition;

import net.astopia.api.backend.command.execution.IExecutionSession;
import org.jetbrains.annotations.NotNull;

public interface ConditionChecker {

    void checkCondition(final @NotNull IExecutionSession session,
                        final @NotNull String meta) throws Exception;

}
