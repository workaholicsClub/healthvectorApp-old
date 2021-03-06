package ru.android.healthvector.domain.development.testing.data.processors.core;

import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.io.Serializable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.utils.strings.TimeUtils;

import static ru.android.healthvector.domain.development.testing.data.processors.core.DomanTestProcessorUtils.ADVANCED_RANGES;
import static ru.android.healthvector.domain.development.testing.data.processors.core.DomanTestProcessorUtils.NORMAL_RANGES;
import static ru.android.healthvector.domain.development.testing.data.processors.core.DomanTestProcessorUtils.SLOW_RANGES;

@Value
public class DomanResult implements Serializable {
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
    @Nullable
    Double percents;
    @Nullable
    StageType stageType;

    @Builder
    public DomanResult(@NonNull LocalDate birthDate,
                       @NonNull LocalDate date,
                       int stage,
                       @Nullable LocalDate domanDate) {
        this.birthDate = birthDate;
        this.date = date;
        this.stage = stage;
        this.domanDate = domanDate;
        percents = domanDate == null ? null : calculatePercents(birthDate, domanDate, stage);
        stageType = domanDate == null ? null : calculateStageType(birthDate, domanDate, stage);
    }

    private static StageType calculateStageType(@NonNull LocalDate birthDate,
                                                @NonNull LocalDate domanDate,
                                                int stage) {
        double months = TimeUtils.getMonths(birthDate, domanDate);
        DomanTestProcessorUtils.Range advancedRange = ADVANCED_RANGES[stage];
        DomanTestProcessorUtils.Range normalRange = NORMAL_RANGES[stage];
        if (months < advancedRange.getTo()) {
            return StageType.ADVANCED;
        } else if (months < normalRange.getTo()) {
            return StageType.NORMAL;
        } else {
            return StageType.SLOW;
        }
    }

    private static double calculatePercents(@NonNull LocalDate birthDate,
                                            @NonNull LocalDate domanDate,
                                            int stage) {
        double months = TimeUtils.getMonths(birthDate, domanDate);
        DomanTestProcessorUtils.Range advancedRange = ADVANCED_RANGES[stage];
        DomanTestProcessorUtils.Range normalRange = NORMAL_RANGES[stage];
        DomanTestProcessorUtils.Range slowRange = SLOW_RANGES[stage];
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

    public enum StageType {
        ADVANCED, NORMAL, SLOW
    }
}
