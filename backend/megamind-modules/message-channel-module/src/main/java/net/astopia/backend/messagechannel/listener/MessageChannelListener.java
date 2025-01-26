package net.astopia.backend.messagechannel.listener;

import net.astopia.api.megamind.event.EventHandler;
import net.astopia.api.megamind.event.Listener;
import net.astopia.api.megamind.event.impl.gamer.GamerJoinEvent;
import net.astopia.api.megamind.event.impl.gamer.GamerMessageChannelEvent;
import net.astopia.api.megamind.event.impl.gamer.GamerQuitEvent;
import net.astopia.backend.megamind.MegamindApp;
import net.astopia.backend.megamind.data.gamer.Gamer;
import net.astopia.backend.messagechannel.manager.MessageManager;

public class MessageChannelListener implements Listener {
    public MessageChannelListener() {
        MegamindApp.INSTANCE.pluginManager().registerListener(this);
    }

    @EventHandler
    public void onJoin(GamerJoinEvent event) {

    }

    @EventHandler
    public void onQuit(GamerQuitEvent event) {
        Gamer gamer = (Gamer) event.getGamer();
        MessageManager.INSTANCE.unloadGamer(gamer);
    }

    @EventHandler
    public void onMessage(GamerMessageChannelEvent event) {

    }
}
