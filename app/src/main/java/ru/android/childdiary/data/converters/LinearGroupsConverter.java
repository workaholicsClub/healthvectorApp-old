package ru.android.childdiary.data.converters;

import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import ru.android.childdiary.domain.interactors.core.LinearGroups;

public class LinearGroupsConverter extends SimpleConverter<LinearGroups> {
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
    protected String map(@NonNull LinearGroups linearGroups) {
        return Stream.of(linearGroups.getTimes())
                .map(TIME_FORMATTER::print)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    protected LinearGroups map(@NonNull String value) {
        String[] parts = value.split(DELIMITER);
        List<LocalTime> times = Stream.of(parts)
                .map(TIME_FORMATTER::parseLocalTime)
                .collect(Collectors.toList());
        return LinearGroups.builder().times(new ArrayList<>(times)).build();
    }
}
