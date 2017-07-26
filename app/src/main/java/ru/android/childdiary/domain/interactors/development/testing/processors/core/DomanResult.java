package ru.android.childdiary.domain.interactors.development.testing.processors.core;

import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.utils.strings.TimeUtils;

import static ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanTestProcessorHelper.ADVANCED_RANGES;
import static ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanTestProcessorHelper.NORMAL_RANGES;
import static ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanTestProcessorHelper.SLOW_RANGES;

@Value
public class DomanResult {
    public static final double ADVANCED = 100f / 3f;
    public static final double NORMAL = 100f / 3f;
    public static final double SLOW = 100f / 3f;

    @NonNull
    LocalDate date;
    double months;
    int stage;
    @NonNull
    Double percents;
    @Nullable
    TimeUtils.Age domanAge;

    @Builder
    public DomanResult(@NonNull LocalDate date,
                       double months,
                       int stage,
                       @Nullable TimeUtils.Age domanAge) {
        this.date = date;
        this.months = months;
        this.stage = stage;
        this.domanAge = domanAge;
        percents = calculatePercents();
    }

    private double calculatePercents() {
        double months = domanAge == null ? this.months : domanAge.getMonths();
        DomanTestProcessorHelper.Range advancedRange = ADVANCED_RANGES[stage];
        DomanTestProcessorHelper.Range normalRange = NORMAL_RANGES[stage];
        DomanTestProcessorHelper.Range slowRange = SLOW_RANGES[stage];
        double percents;
        if (months < advancedRange.getTo()) {
            percents = (months - advancedRange.getFrom()) / (advancedRange.getTo() - advancedRange.getFrom()) * ADVANCED;
        } else if (months < normalRange.getTo()) {
            percents = ADVANCED + (months - normalRange.getFrom()) / (normalRange.getTo() - normalRange.getFrom()) * NORMAL;
        } else {
            percents = ADVANCED + NORMAL + (months - slowRange.getFrom()) / (slowRange.getTo() - slowRange.getFrom()) * SLOW;
        }
        percents = 100 - percents;
        return percents;
    }
}
