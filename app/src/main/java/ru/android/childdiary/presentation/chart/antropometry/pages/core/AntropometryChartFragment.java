package ru.android.childdiary.presentation.chart.antropometry.pages.core;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.chart.core.ChartFragment;

public abstract class AntropometryChartFragment extends ChartFragment<AntropometryChartState, AntropometryChartPresenter> implements AntropometryChartView {
    @BindView(R.id.textViewName)
    TextView textViewName;

    private AntropometryChartState state;

    @Override
    protected final int getLayoutResourceId() {
        return R.layout.fragment_antropometry_chart;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (savedInstanceState == null) {
            getPresenter().init(getChild());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textViewName.setText(getChild().getName());
    }

    @Override
    public void showResults(@NonNull AntropometryChartState state) {
        this.state = state;
        plotResults(state);
    }

    @Override
    protected String getIntentionText() {
        return getContext().getString(R.string.no_antropometry_data);
    }

    @Override
    protected String getXTitle() {
        return getString(R.string.date);
    }
}