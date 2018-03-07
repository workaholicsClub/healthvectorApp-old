package ru.android.healthvector.domain.development.antropometry.data;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.healthvector.domain.core.data.ContentObject;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.utils.ObjectUtils;

@Value
@Builder(toBuilder = true)
public class Antropometry implements Serializable, ContentObject<Antropometry> {
    public static final Antropometry NULL = Antropometry.builder().build();

    Long id;

    Child child;

    Double height;

    Double weight;

    LocalDate date;

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull Antropometry other) {
        return ObjectUtils.equals(height, other.height)
                && ObjectUtils.equals(weight, other.weight)
                && ObjectUtils.equals(date, other.date);
    }
}
