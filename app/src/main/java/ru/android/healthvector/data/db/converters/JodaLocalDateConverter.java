package ru.android.healthvector.data.db.converters;

import org.joda.time.LocalDate;

import java.util.Date;

import io.requery.Converter;
import io.requery.Nullable;

public class JodaLocalDateConverter implements Converter<LocalDate, Long> {
    @Override
    public Class<LocalDate> getMappedType() {
        return LocalDate.class;
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
    public Long convertToPersisted(LocalDate value) {
        return value == null ? null : value.toDate().getTime();
    }

    @Override
    public LocalDate convertToMapped(Class<? extends LocalDate> type, Long value) {
        return value == null ? null : LocalDate.fromDateFields(new Date(value));
    }
}
