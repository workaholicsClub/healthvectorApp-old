package ru.android.childdiary.presentation.chart.antropometry.pages.core;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.development.antropometry.AntropometryPoint;
import ru.android.childdiary.presentation.chart.core.ChartState;

@Value
@Builder(toBuilder = true)
public class AntropometryChartState implements ChartState {
    @NonNull
    List<AntropometryPoint> values;
    @Nullable
    List<AntropometryPoint> lowValues;
    @Nullable
    List<AntropometryPoint> highValues;

    public List<AntropometryPoint> getLowValues() {
        return lowValues == null ? Collections.emptyList() : lowValues;
    }

    public List<AntropometryPoint> getHighValues() {
        return highValues == null ? Collections.emptyList() : highValues;
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }
}
