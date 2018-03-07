package ru.android.healthvector.data.db.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.healthvector.domain.calendar.data.core.PeriodicityType;

public class PeriodicityTypeEnumConverter extends EnumOrdinalConverter<PeriodicityType> {
    public PeriodicityTypeEnumConverter() {
        super(PeriodicityType.class);
    }
}
