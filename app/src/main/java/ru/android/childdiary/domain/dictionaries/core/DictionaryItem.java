package ru.android.childdiary.domain.dictionaries.core;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.android.childdiary.domain.core.LocalizationUtils;

@EqualsAndHashCode
@ToString
public abstract class DictionaryItem {
    public String getName() {
        return LocalizationUtils.getLocalizedName(getNameUser(), getNameEn(), getNameRu());
    }

    public abstract String getNameEn();

    public abstract String getNameRu();

    public abstract String getNameUser();
}
