package ru.android.childdiary.data.converters;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import io.requery.Converter;
import io.requery.Nullable;

public class JodaLocalTimeConverter implements Converter<LocalTime, String> {
    /**
     * DateTimeFormat is thread-safe and immutable, and the formatters it returns are as well.
     * <p>
     *
     * @see <a href="http://www.joda.org/joda-time/apidocs/org/joda/time/format/DateTimeFormat.html">DateTimeFormat</a>
     */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern("HH:mm");

    @Override
    public Class<LocalTime> getMappedType() {
        return LocalTime.class;
    }

    @Override
    public Class<String> getPersistedType() {
        return String.class;
    }

    @Override
    @Nullable
    public Integer getPersistedSize() {
        return null;
    }

    @Override
    public String convertToPersisted(LocalTime value) {
        return value == null ? null : TIME_FORMATTER.print(value);
    }

    @Override
    public LocalTime convertToMapped(Class<? extends LocalTime> type, String value) {
        return value == null ? null : TIME_FORMATTER.parseLocalTime(value);
    }
}
