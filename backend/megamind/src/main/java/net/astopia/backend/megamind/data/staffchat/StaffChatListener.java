package net.astopia.backend.megamind.data.staffchat;

import lombok.val;
import net.astopia.api.backend.permission.Group;
import net.astopia.api.backend.permission.PermissionType;
import net.astopia.api.megamind.event.EventHandler;
import net.astopia.api.megamind.event.Listener;
import net.astopia.api.megamind.event.impl.gamer.GamerJoinEvent;
import net.astopia.api.megamind.event.impl.gamer.GamerQuitEvent;
import net.astopia.backend.base.gamer.CommonGamer;
import net.astopia.backend.base.util.color.ChatColor;
import net.astopia.backend.megamind.MegamindApp;
import net.astopia.backend.megamind.manager.StaffChatManager;

/**
 * @author Vihara
 */
public final class StaffChatListener implements Listener {

    public StaffChatListener() {
        MegamindApp.INSTANCE.pluginManager().registerListener(this);
    }

    @EventHandler
    public void onGamerJoin(final GamerJoinEvent event) {
        val gamer = (CommonGamer) event.getGamer();
        if (gamer.getRanks().getRanks(PermissionType.MEDIA).isEmpty()
                && !gamer.getRanks().canApply(Group.BUILDER)) {
            return;
        }

        StaffChatManager.INSTANCE.sendMessage(gamer.getColoredName() + ChatColor.AQUA + " is now online");
    }

    @EventHandler
    public void onLeave(final GamerQuitEvent event) {
        val gamer = (CommonGamer) event.getGamer();
        if (gamer.getRanks().getRanks(PermissionType.MEDIA).isEmpty()
                && !gamer.getRanks().canApply(Group.BUILDER)) {
            return;
        }

        StaffChatManager.INSTANCE.sendMessage(gamer.getColoredName() + ChatColor.AQUA + " is now offline");
    }

}
