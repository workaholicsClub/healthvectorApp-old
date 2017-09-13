package ru.android.childdiary.domain.dictionaries.doctors.data;

import android.support.annotation.NonNull;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.core.data.ContentObject;
import ru.android.childdiary.domain.dictionaries.core.DictionaryItem;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@Builder
public class Doctor implements Serializable, ContentObject<Doctor>, DictionaryItem {
    public static final Doctor NULL = Doctor.builder().build();

    Long id;

    String nameEn, nameRu, nameUser;

    public String getName() {
        return DictionaryItem.getLocalizedName(this);
    }

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull Doctor other) {
        return ObjectUtils.equals(id, other.id)
                && ObjectUtils.contentEquals(nameEn, other.nameEn)
                && ObjectUtils.contentEquals(nameRu, other.nameRu)
                && ObjectUtils.contentEquals(nameUser, other.nameUser);
    }
}
