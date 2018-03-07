package ru.android.healthvector.data.db.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.healthvector.data.types.Breast;

public class BreastEnumConverter extends EnumOrdinalConverter<Breast> {
    public BreastEnumConverter() {
        super(Breast.class);
    }
}
