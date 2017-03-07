package ru.android.childdiary.data.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.data.types.Sex;

public class EventTypeEnumConverter extends EnumOrdinalConverter<EventType> {
    public EventTypeEnumConverter() {
        super(EventType.class);
    }
}
