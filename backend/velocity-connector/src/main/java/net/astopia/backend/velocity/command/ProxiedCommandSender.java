package net.astopia.backend.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.astopia.api.backend.gamer.ICommandSender;
import net.astopia.backend.velocity.VelocityConnector;
import org.jetbrains.annotations.NotNull;

public interface ProxiedCommandSender extends ICommandSender {

    @Override
    default @NotNull String getNameAsString() {
        return getCommandSender() instanceof Player player ? player.getUsername() : "Console";
    }

    CommandSource getCommandSender();

    @Override
    default void sendMessageAsString(final String s) {
        getCommandSender().sendMessage(Component.text(s));
    }

    @Override
    default void dispatchCommand(final @NonNull String s) {
        VelocityConnector.INSTANCE.server().getCommandManager().executeAsync(getCommandSender(), s);
    }
}
