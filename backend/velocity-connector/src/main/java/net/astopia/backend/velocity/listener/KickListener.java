package net.astopia.backend.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.proxy.Player;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import net.astopia.api.protocol.platform.Platform;
import net.astopia.backend.velocity.gamer.Gamer;
import net.astopia.backend.velocity.util.ServerUtil;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public final class KickListener {
    @Subscribe
    public void onKick(final KickedFromServerEvent event) {
        Player player = event.getPlayer();

/*        String reason = event.getServerKickReason().get().toString();*/

        var gamer = Gamer.getGamer(player);

        if (gamer == null) {
            Platform.logger.info("Unlogged player {} was kicked from", player.getUsername());
            Platform.logger.info("> {}, v{}", event.getServer().getServerInfo().getName(), player.getProtocolVersion());
/*
            Logging.IMP.info("> {}", reason);
*/
/*
            eventki(new TextComponent[]{new TextComponent(
                    "§cInternal server error.\n" +
                            "§c[B-UNLOGGED]")});*/
            return;
        }


// TODO: write a shit that will move a player to HUB or game lobby server on game-server shutdown

        if (event.getResult() instanceof KickedFromServerEvent.RedirectPlayer) {
            var kickedFrom = event.getServer().getServerInfo();
            val kickTo = ServerUtil.getRandomLobbyServer();
            //event.setResult(KickedFromServerEvent.ServerKickResult.);
            event.setResult(KickedFromServerEvent.RedirectPlayer.create(kickTo));

            gamer.sendLocalizedMessage(false, "fallback_server",
                    "%kick_to_name%", kickTo.getServerInfo().getName(), "%reason%", "none");
        }
    }

}
