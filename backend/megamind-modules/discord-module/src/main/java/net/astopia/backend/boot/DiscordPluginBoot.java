package net.astopia.backend.boot;

import net.astopia.backend.DiscordPlugin;
import net.astopia.backend.discord.DiscordBot;
import net.astopia.backend.megamind.plugin.Plugin;

public class DiscordPluginBoot extends Plugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();

        DiscordPlugin.INSTANCE.init(this);
        DiscordBot.INSTANCE.init();
    }

    @Override
    public void onDisable() {
    }
}