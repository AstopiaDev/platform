package net.astopia.backend.base.gamer;

import net.astopia.api.backend.gamer.IGamer;
import org.jetbrains.annotations.NotNull;

public interface IBaseGamer extends IGamer {
    <T> T getProperty(@NotNull String key);

    /**
     * Добавить значение к существующей проперти
     */
    void addProperty(String key, int value);

    /**
     * Установить значение для проперти
     */
    <T> void setProperty(String key, T value);

    void removeProperty(String key);
}
