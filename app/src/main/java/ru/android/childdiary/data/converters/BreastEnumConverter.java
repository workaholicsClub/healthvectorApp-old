package ru.android.childdiary.data.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.childdiary.data.types.Breast;

public class BreastEnumConverter extends EnumOrdinalConverter<Breast> {
    public BreastEnumConverter() {
        super(Breast.class);
    }
}
