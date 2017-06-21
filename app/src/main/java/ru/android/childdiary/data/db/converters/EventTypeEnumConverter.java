package ru.android.childdiary.data.db.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.childdiary.data.types.EventType;

public class EventTypeEnumConverter extends EnumOrdinalConverter<EventType> {
    public EventTypeEnumConverter() {
        super(EventType.class);
    }
}
