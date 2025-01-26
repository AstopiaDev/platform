package net.astopia.api.megamind.event.impl.gamer;

import lombok.NonNull;
import net.astopia.api.backend.permission.IUserRank;
import net.astopia.api.megamind.gamer.IMegamindGamer;

/**
 * @author Vihara
 */
public final class PlayerRankAddEvent extends RankChangedEvent {

    public PlayerRankAddEvent(final @NonNull IMegamindGamer gamer,
                              final @NonNull IUserRank rank) {
        super(gamer, rank);
    }
}
