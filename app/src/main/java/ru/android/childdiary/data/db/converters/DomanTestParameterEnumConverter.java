package ru.android.childdiary.data.db.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.childdiary.data.types.DomanTestParameter;

public class DomanTestParameterEnumConverter extends EnumOrdinalConverter<DomanTestParameter> {
    public DomanTestParameterEnumConverter() {
        super(DomanTestParameter.class);
    }
}
