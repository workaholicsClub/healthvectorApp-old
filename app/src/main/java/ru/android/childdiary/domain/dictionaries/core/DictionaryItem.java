package ru.android.childdiary.domain.dictionaries.core;

import ru.android.childdiary.domain.core.LocalizationUtils;

public abstract class DictionaryItem {
    public String getName() {
        return LocalizationUtils.getLocalizedName(getNameUser(), getNameEn(), getNameRu());
    }

    public abstract String getNameEn();

    public abstract String getNameRu();

    public abstract String getNameUser();
}
