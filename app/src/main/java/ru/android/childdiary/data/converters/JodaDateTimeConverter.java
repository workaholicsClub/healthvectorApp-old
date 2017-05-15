package ru.android.childdiary.data.converters;

import org.joda.time.DateTime;

import io.requery.Converter;
import io.requery.Nullable;

public class JodaDateTimeConverter implements Converter<DateTime, Long> {
    @Override
    public Class<DateTime> getMappedType() {
        return DateTime.class;
    }

    @Override
    public Class<Long> getPersistedType() {
        return Long.class;
    }

    @Override
    @Nullable
    public Integer getPersistedSize() {
        return null;
    }

    @Override
    public Long convertToPersisted(DateTime value) {
        return value == null ? null : value.toDate().getTime();
    }

    @Override
    public DateTime convertToMapped(Class<? extends DateTime> type, Long value) {
        return value == null ? null : new DateTime(value);
    }
}
