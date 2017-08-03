package ru.android.childdiary.data.db.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.childdiary.domain.interactors.calendar.data.core.TimeUnit;

public class TimeUnitEnumConverter extends EnumOrdinalConverter<TimeUnit> {
    public TimeUnitEnumConverter() {
        super(TimeUnit.class);
    }
}
