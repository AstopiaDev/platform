package net.astopia.backend.messagechannel;

import net.astopia.backend.megamind.MegamindApp;
import net.astopia.backend.megamind.plugin.Plugin;
import net.astopia.backend.messagechannel.command.AppendCommand;
import net.astopia.backend.messagechannel.command.MessageCommand;
import net.astopia.backend.messagechannel.command.ReplyCommand;
import net.astopia.backend.messagechannel.listener.MessageChannelListener;

public class MessageChannelPlugin extends Plugin {
    @Override
    public void onEnable() {
        new MessageChannelListener();
        MegamindApp.INSTANCE.commandManager().addCommand(AppendCommand.class);
        MegamindApp.INSTANCE.commandManager().addCommand(MessageCommand.class);
        MegamindApp.INSTANCE.commandManager().addCommand(ReplyCommand.class);
    }

    @Override
    public void onDisable() {
        MegamindApp.INSTANCE.removeCommand("append");
        MegamindApp.INSTANCE.removeCommand("message");
        MegamindApp.INSTANCE.removeCommand("reply");
    }
}
