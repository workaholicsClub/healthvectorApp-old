package ru.android.childdiary.domain.interactors.development.testing.processors.core;

import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import static ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanTestProcessorHelper.ADVANCED_RANGES;
import static ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanTestProcessorHelper.NORMAL_RANGES;
import static ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanTestProcessorHelper.SLOW_RANGES;

@Value
public class DomanResult {
    public static final double ADVANCED = 100f / 3f;
    public static final double NORMAL = 100f / 3f;
    public static final double SLOW = 100f / 3f;

    @NonNull
    LocalDate birthDate;
    @NonNull
    LocalDate date;
    int stage;
    @Nullable
    LocalDate domanDate;
    @NonNull
    Double percents;

    @Builder
    public DomanResult(@NonNull LocalDate birthDate,
                       @NonNull LocalDate date,
                       int stage,
                       @Nullable LocalDate domanDate) {
        this.birthDate = birthDate;
        this.date = date;
        this.stage = stage;
        this.domanDate = domanDate;
        percents = calculatePercents();
    }

    private double calculatePercents() {
        LocalDate toDate = domanDate == null ? date : domanDate;
        double months = DomanTestProcessorHelper.getMonths(birthDate, toDate);
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
