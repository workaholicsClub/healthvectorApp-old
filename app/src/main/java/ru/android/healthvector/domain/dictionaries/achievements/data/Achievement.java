package ru.android.healthvector.domain.dictionaries.achievements.data;

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
public class Achievement extends DictionaryItem implements Serializable, ContentObject<Achievement> {
    public static final Achievement NULL = Achievement.builder().build();

    Long id;

    String nameEn, nameRu, nameUser;

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull Achievement other) {
        return ObjectUtils.equals(getId(), other.getId())
                && ObjectUtils.contentEquals(getName(), other.getName());
    }
}
