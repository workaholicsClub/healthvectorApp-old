package ru.android.childdiary.data.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.childdiary.domain.interactors.core.TimeUnit;

public class TimeUnitEnumConverter extends EnumOrdinalConverter<TimeUnit> {
    public TimeUnitEnumConverter() {
        super(TimeUnit.class);
    }
}
