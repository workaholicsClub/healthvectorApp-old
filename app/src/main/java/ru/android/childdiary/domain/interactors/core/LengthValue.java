package ru.android.childdiary.domain.interactors.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.core.ContentObject;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@Builder(toBuilder = true)
public class LengthValue implements Serializable, ContentObject<LengthValue> {
    @Nullable
    Integer length;

    @Nullable
    TimeUnit timeUnit;

    @Override
    public boolean isContentEmpty() {
        return !ObjectUtils.isPositive(length) || timeUnit == null;
    }

    @Override
    public boolean isContentEqual(@NonNull LengthValue other) {
        return ObjectUtils.equals(length, other.length)
                && timeUnit == other.timeUnit;
    }
}
