package ru.android.healthvector.data.db.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.healthvector.data.types.EventType;

public class EventTypeEnumConverter extends EnumOrdinalConverter<EventType> {
    public EventTypeEnumConverter() {
        super(EventType.class);
    }
}
