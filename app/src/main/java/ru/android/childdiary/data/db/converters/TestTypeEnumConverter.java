package ru.android.childdiary.data.db.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.childdiary.data.types.TestType;

public class TestTypeEnumConverter extends EnumOrdinalConverter<TestType> {
    public TestTypeEnumConverter() {
        super(TestType.class);
    }
}
