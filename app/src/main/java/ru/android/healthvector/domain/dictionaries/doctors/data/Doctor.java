package ru.android.healthvector.domain.dictionaries.doctors.data;

import android.support.annotation.NonNull;

import java.io.Serializable;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.android.healthvector.domain.core.data.ContentObject;
import ru.android.healthvector.domain.dictionaries.core.DictionaryItem;
import ru.android.healthvector.utils.ObjectUtils;

@Value
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Doctor extends DictionaryItem implements Serializable, ContentObject<Doctor> {
    public static final Doctor NULL = Doctor.builder().build();

    Long id;

    String nameEn, nameRu, nameUser;

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull Doctor other) {
        return ObjectUtils.equals(getId(), other.getId())
                && ObjectUtils.contentEquals(getName(), other.getName());
    }
}
