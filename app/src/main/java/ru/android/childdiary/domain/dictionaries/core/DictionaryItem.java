package ru.android.childdiary.domain.dictionaries.core;

import java.util.Locale;

public interface DictionaryItem {
    static String getLocalizedName(DictionaryItem object) {
        if (object.getNameUser() != null) {
            return object.getNameUser();
        }
        boolean isRu = Locale.getDefault().getLanguage().equals(new Locale("ru").getLanguage());
        if (isRu) {
            return object.getNameRu();
        }
        return object.getNameEn();
    }

    String getNameEn();

    String getNameRu();

    String getNameUser();
}
