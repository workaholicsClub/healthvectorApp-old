package ru.android.childdiary.domain.interactors.medical.core;

import android.support.annotation.NonNull;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.core.ContentObject;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@Builder(toBuilder = true)
public class MedicineMeasure implements Serializable, ContentObject<MedicineMeasure> {
    public static final MedicineMeasure NULL = MedicineMeasure.builder().build();

    Long id;

    String name;

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
