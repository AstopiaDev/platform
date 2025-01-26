package net.astopia.backend.votifier.util.standalone;

import net.astopia.backend.votifier.model.Vote;

public interface VoteReceiver {
    void onVote(Vote vote) throws Exception;
    void onException(Throwable throwable);
}
