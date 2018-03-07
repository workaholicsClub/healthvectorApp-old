package ru.android.healthvector.data.db.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.healthvector.data.types.DomanTestParameter;

public class DomanTestParameterEnumConverter extends EnumOrdinalConverter<DomanTestParameter> {
    public DomanTestParameterEnumConverter() {
        super(DomanTestParameter.class);
    }
}
