package ru.android.childdiary.domain.interactors.development.testing.processors.core;

import android.support.annotation.NonNull;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;

import lombok.AllArgsConstructor;
import lombok.Value;
import ru.android.childdiary.utils.strings.TimeUtils;

class DomanTestProcessorHelper {
    static final Range[] ADVANCED_RANGES = new Range[]{
            new Range(0.25, 0.75),
            new Range(0.625, 1.875),
            new Range(1.75, 5.25),
            new Range(3, 9),
            new Range(4.50, 13.5),
            new Range(9, 27),
            new Range(18, 54)
    };
    static final Range[] NORMAL_RANGES = new Range[]{
            new Range(0.75, 1.50),
            new Range(1.875, 3.75),
            new Range(5.25, 10.5),
            new Range(9, 18),
            new Range(13.5, 27),
            new Range(27, 54),
            new Range(54, 108)
    };
    static final Range[] SLOW_RANGES = new Range[]{
            new Range(1.5, 2.5),
            new Range(3.75, 6.25),
            new Range(10.5, 17.5),
            new Range(18, 30),
            new Range(27, 45),
            new Range(54, 90),
            new Range(108, 180)
    };

    public static double getMonths(@NonNull LocalDate birthDate, @NonNull LocalDate date) {
        if (birthDate.isAfter(date)) {
            return 0;
        }
        int months = Months.monthsBetween(birthDate, date).getMonths();
        int days = Days.daysBetween(birthDate.plusMonths(months), date).getDays();
        double part = days / 30.0;
        if (part < 0) {
            part = 0;
        } else if (part > 1) {
            part = 1;
        }
        return months + part;
    }

    public static double getMonths(@NonNull TimeUtils.Age age) {
        return age.getMonths();
    }

    public static int getIndex(double months) {
        int length = NORMAL_RANGES.length;
        int lastIndex = length - 1;
        for (int i = 0; i < length; ++i) {
            Range range = NORMAL_RANGES[i];
            if (i == 0 && months < range.getTo()
                    || i == lastIndex && months >= range.getFrom()
                    || range.contains(months)) {
                return i;
            }
            // Костыль для разрывных диапазонов
            Range nextRange = i < lastIndex ? NORMAL_RANGES[i + 1] : null;
            if (nextRange != null && range.getTo() <= months && months < nextRange.getFrom()) {
                double first = months - range.getTo();
                double second = nextRange.getFrom() - months;
                if (first <= second) {
                    return i;
                } else {
                    return i + 1;
                }
            }
        }
        if (months < NORMAL_RANGES[0].getFrom()) {
            return 0;
        }
        return lastIndex;
    }

    @Value
    @AllArgsConstructor(suppressConstructorProperties = true)
    static class Range {
        double from, to;

        public boolean contains(double value) {
            return from <= value && value < to;
        }
    }
}
