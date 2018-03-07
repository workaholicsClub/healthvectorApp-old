package ru.android.healthvector.data.db.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.healthvector.data.types.FeedType;

public class FeedTypeEnumConverter extends EnumOrdinalConverter<FeedType> {
    public FeedTypeEnumConverter() {
        super(FeedType.class);
    }
}
