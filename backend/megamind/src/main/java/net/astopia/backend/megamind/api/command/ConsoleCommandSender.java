package net.astopia.backend.megamind.api.command;

import net.astopia.api.backend.permission.UserRankContainer;
import net.astopia.api.protocol.platform.Platform;
import net.astopia.backend.base.language.Language;
import net.astopia.backend.base.permission.UserRank;
import org.jetbrains.annotations.NotNull;

public enum ConsoleCommandSender implements CommandSender {
    INSTANCE;

    private final String name = "Console";

    @Override
    public @NotNull String getNameAsString() {
        return name;
    }

    @Override
    public Language getLanguage() {
        return Language.DEFAULT;
    }

    @Override
    public String getDisplayNameAsString() {
        return name;
    }

    @Override
    public void sendMessageAsString(String message) {
        Platform.logger.info(message);
    }

    @Override
    public void sendRawMessage(String message) {
        Platform.logger.info(message);
    }

    @Override
    public boolean isConsole() {
        return true;
    }

    @Override
    public UserRankContainer<UserRank> getRanks() {
        return null;
    }

    @Override
    public boolean isStaff() {
        return true;
    }

}
