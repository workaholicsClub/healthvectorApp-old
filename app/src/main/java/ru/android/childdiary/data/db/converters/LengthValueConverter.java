package ru.android.childdiary.data.db.converters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.android.childdiary.domain.interactors.core.LengthValue;
import ru.android.childdiary.domain.interactors.core.TimeUnit;

public class LengthValueConverter extends SimpleConverter<LengthValue> {
    private static final String DELIMITER = ";";

    @Override
    public Class<LengthValue> getMappedType() {
        return LengthValue.class;
    }

    @Override
    @Nullable
    protected String map(@NonNull LengthValue value) {
        return toString(value.getLength())
                + DELIMITER + toString(value.getTimeUnit());
    }

    @Override
    @Nullable
    protected LengthValue map(@NonNull String value) {
        String parts[] = value.split(DELIMITER);
        Integer length = toInteger(parts[0]);
        TimeUnit timeUnit = toEnum(TimeUnit.class, parts[1]);
        return LengthValue.builder().length(length).timeUnit(timeUnit).build();
    }
}
