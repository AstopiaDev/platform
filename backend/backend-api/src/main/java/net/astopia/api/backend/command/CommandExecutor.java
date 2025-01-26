package net.astopia.api.backend.command;

import net.astopia.api.backend.command.execution.IExecutionSession;

public interface CommandExecutor<Session extends IExecutionSession> {

    void execute(Session session) throws Exception;

    @SuppressWarnings("unchecked")
    default CommandExecutor<IExecutionSession> unchecked() {
        return session -> execute((Session) session);
    }

}
