package ru.android.childdiary.data.db.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.childdiary.data.types.FeedType;

public class FeedTypeEnumConverter extends EnumOrdinalConverter<FeedType> {
    public FeedTypeEnumConverter() {
        super(FeedType.class);
    }
}
