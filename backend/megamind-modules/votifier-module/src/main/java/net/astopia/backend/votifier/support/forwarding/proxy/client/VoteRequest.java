package net.astopia.backend.votifier.support.forwarding.proxy.client;

import net.astopia.backend.votifier.model.Vote;

public class VoteRequest {
    private final String challenge;
    private final Vote vote;

    public VoteRequest(String challenge, Vote vote) {
        this.challenge = challenge;
        this.vote = vote;
    }

    public String getChallenge() {
        return challenge;
    }

    public Vote getVote() {
        return vote;
    }

    @Override
    public String toString() {
        return "VoteRequest{" +
                "challenge='" + challenge + '\'' +
                ", vote=" + vote +
                '}';
    }
}
