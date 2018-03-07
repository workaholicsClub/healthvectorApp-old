package ru.android.healthvector.presentation.chart.antropometry.pages;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.github.mikephil.charting.charts.CombinedChart;

import lombok.Getter;
import ru.android.healthvector.R;
import ru.android.healthvector.presentation.chart.antropometry.core.AntropometryChartPlotter;
import ru.android.healthvector.presentation.chart.antropometry.pages.core.AntropometryChartFragment;
import ru.android.healthvector.presentation.chart.antropometry.pages.core.AntropometryChartState;
import ru.android.healthvector.presentation.chart.core.ChartPlotter;

public class HeightChartFragment extends AntropometryChartFragment {
    @Getter
    @InjectPresenter
    HeightChartPresenter presenter;

    @Override
    protected ChartPlotter getChartPlotter(@NonNull CombinedChart chart, @NonNull AntropometryChartState state) {
        return new AntropometryChartPlotter(chart, new HeightValueFormatter(), 0.5f, state.getChild().getBirthDate(), state.getData());
    }

    @Override
    protected String getYTitle() {
        return getString(R.string.height);
    }
}
