package ru.android.childdiary.presentation.chart.antropometry.pages;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.github.mikephil.charting.charts.CombinedChart;

import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.chart.antropometry.core.AntropometryChartPlotter;
import ru.android.childdiary.presentation.chart.antropometry.pages.core.AntropometryChartFragment;
import ru.android.childdiary.presentation.chart.antropometry.pages.core.AntropometryChartState;
import ru.android.childdiary.presentation.chart.core.ChartPlotter;

public class WeightChartFragment extends AntropometryChartFragment {
    @Getter
    @InjectPresenter
    WeightChartPresenter presenter;

    @Override
    protected ChartPlotter getChartPlotter(@NonNull CombinedChart chart, @NonNull AntropometryChartState state) {
        return new AntropometryChartPlotter(chart, state.getValues(), state.getLowValues(), state.getHighValues(), new WeightValueFormatter());
    }

    @Override
    protected String getYTitle() {
        return getString(R.string.weight);
    }
}
