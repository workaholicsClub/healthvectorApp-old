package ru.android.childdiary.data.db.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.childdiary.domain.interactors.calendar.data.core.PeriodicityType;

public class PeriodicityTypeEnumConverter extends EnumOrdinalConverter<PeriodicityType> {
    public PeriodicityTypeEnumConverter() {
        super(PeriodicityType.class);
    }
}
