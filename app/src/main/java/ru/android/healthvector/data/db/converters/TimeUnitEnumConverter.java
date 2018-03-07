package ru.android.healthvector.data.db.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.healthvector.domain.calendar.data.core.TimeUnit;

public class TimeUnitEnumConverter extends EnumOrdinalConverter<TimeUnit> {
    public TimeUnitEnumConverter() {
        super(TimeUnit.class);
    }
}
