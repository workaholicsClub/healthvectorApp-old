package ru.android.healthvector.data.db.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.healthvector.data.types.TestType;

public class TestTypeEnumConverter extends EnumOrdinalConverter<TestType> {
    public TestTypeEnumConverter() {
        super(TestType.class);
    }
}
