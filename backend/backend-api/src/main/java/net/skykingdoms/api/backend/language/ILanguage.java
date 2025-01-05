package net.skykingdoms.api.backend.language;

import lombok.NonNull;
import net.kyori.adventure.text.Component;

import java.util.List;

public interface ILanguage {
    String getName();
    String getMessage(@NonNull String key);
    Component getMessageComponent(@NonNull String key);
    String getMessage(@NonNull String key, Object... objects);
    Component getMessageComponent(@NonNull String key, Object... objects);
    String getMessage(boolean modern, @NonNull String key, Object... objects);
    Component getMessageComponent(boolean modern, @NonNull String key, Object... objects);
    String getMessage(boolean modern, @NonNull String key, boolean useDefault, Object[] objects);
    Component getMessageComponent(boolean modern, @NonNull String key, boolean useDefault, Object[] objects);
    List<String> getMessageList(@NonNull String key, Object... objects);
    List<Component> getMessageComponentList(@NonNull String key, Object... objects);
    List<String> getMessageList(boolean modern, @NonNull String key, Object... objects);
    List<Component> getMessageComponentList(boolean modern, @NonNull String key, Object... objects);
    List<String> getMessageList(boolean modern, @NonNull String key, boolean useDefault, Object[] objects);
    List<Component> getMessageComponentList(boolean modern, @NonNull String key, boolean useDefault, Object[] objects);
}
