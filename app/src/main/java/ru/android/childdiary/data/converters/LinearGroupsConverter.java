package ru.android.childdiary.data.converters;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
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
        List<String> strings = Observable.fromIterable(linearGroups.getTimes())
                .map(TIME_FORMATTER::print)
                .toList()
                .blockingGet();
        return TextUtils.join(DELIMITER, strings);
    }

    @Override
    protected LinearGroups map(@NonNull String value) {
        String[] parts = value.split(DELIMITER);
        List<LocalTime> times = Observable.fromArray(parts)
                .map(TIME_FORMATTER::parseLocalTime)
                .toList()
                .blockingGet();
        return LinearGroups.builder().times(new ArrayList<>(times)).build();
    }
}
