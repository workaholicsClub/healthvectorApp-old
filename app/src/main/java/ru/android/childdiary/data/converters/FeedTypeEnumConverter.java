package ru.android.childdiary.data.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.data.types.Sex;

public class FeedTypeEnumConverter extends EnumOrdinalConverter<FeedType> {
    public FeedTypeEnumConverter() {
        super(FeedType.class);
    }
}
