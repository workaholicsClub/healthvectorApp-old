package ru.android.healthvector.domain.calendar.data.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.healthvector.domain.core.data.ContentObject;
import ru.android.healthvector.utils.ObjectUtils;
import ru.android.healthvector.utils.strings.TimeUtils;

@Value
@Builder
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

    public int getMinutes() {
        if (isContentEmpty() || timeUnit == null || length == null) {
            return 0;
        }

        // если понадобится перенос на месяцы, то надо менять логику в слое данных:
        // переносить на календарное количество дней в месяце, а не на минуты
        switch (timeUnit) {
            case MINUTE:
                return length;
            case HOUR:
                return length * TimeUtils.MINUTES_IN_HOUR;
            case DAY:
                return length * TimeUtils.MINUTES_IN_DAY;
            case WEEK:
                return length * TimeUtils.MINUTES_IN_DAY * 7;
            default:
                throw new IllegalArgumentException("Unsupported time unit");
        }
    }
}
