package net.astopia.api.backend.language;

import lombok.NonNull;

public interface ILanguageContext {
    <T extends ILanguage> String format(final @NonNull T language);
}
