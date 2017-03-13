package ru.android.childdiary.data.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.childdiary.data.types.Breast;

public class DiaperStateEnumConverter extends EnumOrdinalConverter<Breast> {
    public DiaperStateEnumConverter() {
        super(Breast.class);
    }
}