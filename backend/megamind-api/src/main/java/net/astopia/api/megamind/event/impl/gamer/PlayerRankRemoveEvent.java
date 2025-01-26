package net.astopia.api.megamind.event.impl.gamer;

import lombok.NonNull;
import net.astopia.api.backend.permission.IUserRank;
import net.astopia.api.megamind.gamer.IMegamindGamer;

public final class PlayerRankRemoveEvent extends RankChangedEvent {

    public PlayerRankRemoveEvent(final @NonNull IMegamindGamer gamer,
                                 final @NonNull IUserRank rank) {
        super(gamer, rank);
    }
}
