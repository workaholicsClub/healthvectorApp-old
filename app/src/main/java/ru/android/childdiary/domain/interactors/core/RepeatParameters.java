package ru.android.childdiary.domain.interactors.core;

import android.support.annotation.NonNull;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.core.ContentObject;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@Builder(toBuilder = true)
public class RepeatParameters implements Serializable, ContentObject<RepeatParameters> {
    Long id;

    LinearGroups frequency;

    PeriodicityType periodicity;

    LengthValue length;

    @Override
    public boolean isContentEmpty() {
        return ObjectUtils.isEmpty(frequency)
                && periodicity == null
                && ObjectUtils.isEmpty(length);
    }

    @Override
    public boolean isContentEqual(@NonNull RepeatParameters other) {
        return ObjectUtils.contentEquals(frequency, other.frequency)
                && periodicity == other.periodicity
                && ObjectUtils.contentEquals(length, other.length);
    }
}
