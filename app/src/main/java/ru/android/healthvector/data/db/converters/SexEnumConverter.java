package ru.android.healthvector.data.db.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.healthvector.data.types.Sex;

public class SexEnumConverter extends EnumOrdinalConverter<Sex> {
    public SexEnumConverter() {
        super(Sex.class);
    }
}
