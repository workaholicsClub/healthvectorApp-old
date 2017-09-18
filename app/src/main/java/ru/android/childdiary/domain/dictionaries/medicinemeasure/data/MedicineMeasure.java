package ru.android.childdiary.domain.dictionaries.medicinemeasure.data;

import android.support.annotation.NonNull;

import java.io.Serializable;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.domain.core.data.ContentObject;
import ru.android.childdiary.domain.dictionaries.core.DictionaryItem;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MedicineMeasure extends DictionaryItem implements Serializable, ContentObject<MedicineMeasure> {
    public static final MedicineMeasure NULL = MedicineMeasure.builder().build();

    Long id;

    String nameEn, nameRu, nameUser;

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull MedicineMeasure other) {
        return ObjectUtils.equals(getId(), other.getId())
                && ObjectUtils.contentEquals(getName(), other.getName());
    }
}
