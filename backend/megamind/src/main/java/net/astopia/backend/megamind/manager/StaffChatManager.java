package net.astopia.backend.megamind.manager;

import lombok.NonNull;
import lombok.val;
import net.astopia.backend.base.language.Language;
import net.astopia.backend.base.language.LanguageContext;
import net.astopia.backend.base.util.color.ChatColor;
import net.astopia.backend.base.util.string.ObjectLinkedBuf;
import net.astopia.backend.megamind.api.command.ConsoleCommandSender;
import net.astopia.backend.megamind.data.gamer.Gamer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

/**
 * @author Vihara
 */
public final class StaffChatManager {

    public static final StaffChatManager INSTANCE = new StaffChatManager();

    public void sendMessage(final @NonNull String message) {
        sendMessage(new LanguageContext($ -> message), null);
    }

    public void sendMessage(final @NonNull String localizationKey,
                            final @NonNull Object... params
    ) {
        ObjectLinkedBuf array = new ObjectLinkedBuf(params);

        sendMessage(new LanguageContext(localizationKey, lang -> array), null);
    }

    public void sendMessage(final @NonNull LanguageContext message,
                            @Nullable Predicate<Gamer> gamerPredicate) {
        if (gamerPredicate == null) {
            gamerPredicate = gamer -> true;
        }

        for (val gamer : Gamer.getGamers()) {
            if (!gamer.isStaff()) {
                continue;
            }

            if (!gamerPredicate.test(gamer)) {
                continue;
            }

            gamer.sendMessageAsString(ChatColor.AQUA + "[Staff]: " + message.format(gamer.getLanguage()));
        }

        ConsoleCommandSender.INSTANCE.sendMessageAsString(ChatColor.AQUA + "[Staff]: " + message.format(Language.DEFAULT));
    }

}
