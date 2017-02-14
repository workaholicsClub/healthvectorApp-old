package ru.android.childdiary.data.converters;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import io.requery.Converter;

public class JodaLocalDateConverter implements Converter<LocalDate, String> {
    /**
     * DateTimeFormat is thread-safe and immutable, and the formatters it returns are as well.
     * <p>
     *
     * @see <a href="http://www.joda.org/joda-time/apidocs/org/joda/time/format/DateTimeFormat.html">DateTimeFormat</a>
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Override
    public Class<LocalDate> getMappedType() {
        return LocalDate.class;
    }

    @Override
    public Class<String> getPersistedType() {
        return String.class;
    }

    @Override
    public Integer getPersistedSize() {
        return null;
    }

    @Override
    public String convertToPersisted(LocalDate value) {
        return value == null ? null : DATE_FORMATTER.print(value);
    }

    @Override
    public LocalDate convertToMapped(Class<? extends LocalDate> type, String value) {
        return value == null ? null : DATE_FORMATTER.parseLocalDate(value);
    }
}
