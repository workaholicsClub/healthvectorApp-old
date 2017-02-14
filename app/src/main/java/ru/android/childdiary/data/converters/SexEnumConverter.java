package ru.android.childdiary.data.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.childdiary.data.entities.child.Sex;

public class SexEnumConverter extends EnumOrdinalConverter<Sex> {
    public SexEnumConverter() {
        super(Sex.class);
    }
}
