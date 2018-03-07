package ru.android.healthvector.data.db.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.healthvector.data.types.Breast;

public class DiaperStateEnumConverter extends EnumOrdinalConverter<Breast> {
    public DiaperStateEnumConverter() {
        super(Breast.class);
    }
}
