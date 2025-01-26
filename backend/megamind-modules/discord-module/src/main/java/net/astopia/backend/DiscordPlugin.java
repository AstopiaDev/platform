package net.astopia.backend;

import lombok.Getter;
import net.astopia.backend.boot.DiscordPluginBoot;
import net.astopia.backend.command.LinkMinecraftCommand;
import net.astopia.backend.command.UnlinkMinecraftCommand;
import net.astopia.backend.listener.RankListener;
import net.astopia.backend.megamind.MegamindApp;

@Getter
public enum DiscordPlugin {
    INSTANCE;

    DiscordPluginBoot plugin;

    public void init(DiscordPluginBoot plugin) {
        this.plugin = plugin;
        new RankListener();
        MegamindApp.INSTANCE.commandManager().addCommand(LinkMinecraftCommand.class);
        MegamindApp.INSTANCE.commandManager().addCommand(UnlinkMinecraftCommand.class);
    }
}
