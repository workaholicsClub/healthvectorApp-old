package ru.android.childdiary.data.converters;

import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import io.requery.Converter;
import ru.android.childdiary.data.entities.calendar.events.core.LinearGroups;

public class LinearGroupsConverter implements Converter<LinearGroups, String> {
    private static final String DELIMITER = ",";
    /**
     * DateTimeFormat is thread-safe and immutable, and the formatters it returns are as well.
     * <p>
     *
     * @see <a href="http://www.joda.org/joda-time/apidocs/org/joda/time/format/DateTimeFormat.html">DateTimeFormat</a>
     */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern("HH:mm");

    @Override
    public Class<LinearGroups> getMappedType() {
        return LinearGroups.class;
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
    public String convertToPersisted(LinearGroups value) {
        return value == null ? null : map(value);
    }

    @Override
    public LinearGroups convertToMapped(Class<? extends LinearGroups> type, String value) {
        return value == null ? LinearGroups.builder().times(new ArrayList<>()).build() : map(value);
    }

    private static String map(@NonNull LinearGroups linearGroups) {
        return Stream.of(linearGroups.getTimes())
                .map(TIME_FORMATTER::print)
                .collect(Collectors.joining(DELIMITER));
    }

    private static LinearGroups map(@NonNull String value) {
        String[] parts = value.split(DELIMITER);
        List<LocalTime> times = Stream.of(parts)
                .map(TIME_FORMATTER::parseLocalTime)
                .collect(Collectors.toList());
        return LinearGroups.builder().times(new ArrayList<>(times)).build();
    }
}
