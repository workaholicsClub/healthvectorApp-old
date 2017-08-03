package ru.android.childdiary.presentation.chart.antropometry.pages.core;

import android.support.annotation.Nullable;

import com.github.mikephil.charting.data.CombinedData;

import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.domain.interactors.development.antropometry.data.AntropometryPoint;
import ru.android.childdiary.presentation.chart.core.ChartState;

@Value
@Builder(toBuilder = true)
public class AntropometryChartState implements ChartState {
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @NonNull
    List<AntropometryPoint> values;
    @Nullable
    List<AntropometryPoint> lowValues;
    @Nullable
    List<AntropometryPoint> highValues;
    @NonNull
    Child child;
    @Nullable
    CombinedData data;

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

    @Override
    public boolean noChartData() {
        return false;
    }
}
