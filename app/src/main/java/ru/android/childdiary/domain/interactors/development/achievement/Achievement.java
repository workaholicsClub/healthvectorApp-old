package ru.android.childdiary.domain.interactors.development.achievement;

import android.support.annotation.NonNull;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.core.ContentObject;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@Builder
public class Achievement implements Serializable, ContentObject<Achievement> {
    public static final Achievement NULL = Achievement.builder().build();

    Long id;

    String name;

    Boolean isPredefined;

    Integer orderNumber;

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
