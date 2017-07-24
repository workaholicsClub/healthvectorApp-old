package ru.android.childdiary.presentation.chart.testing.pages.core;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.mikephil.charting.charts.CombinedChart;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.presentation.chart.core.ChartFragment;
import ru.android.childdiary.presentation.chart.core.ChartPlotter;
import ru.android.childdiary.presentation.chart.testing.TestChartActivity;
import ru.android.childdiary.presentation.chart.testing.core.TestChartPlotter;
import ru.android.childdiary.presentation.chart.testing.dialogs.ParameterDialogArguments;
import ru.android.childdiary.presentation.chart.testing.dialogs.ParameterDialogFragment;

public abstract class DomanChartFragment extends ChartFragment<DomanChartState, DomanChartPresenter> implements DomanChartView,
        ParameterDialogFragment.Listener {
    private static final String TAG_PARAMETER_DIALOG = "TAG_PARAMETER_DIALOG";

    private DomanChartState state;

    @Override
    protected final int getLayoutResourceId() {
        return R.layout.fragment_doman_chart;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (savedInstanceState == null) {
            getPresenter().init(getChild(), getTestType());
        }
    }

    @Override
    public void showResults(@NonNull DomanChartState state) {
        this.state = state;
        plotResults(state);
        ((TestChartActivity) getActivity()).updateTitle(state.getTestType(), state.getTestParameter());
    }

    public void showFilter() {
        getPresenter().showFilter();
    }

    @Override
    public void specifyParameter(@NonNull ParameterDialogArguments arguments) {
        ParameterDialogFragment fragment = new ParameterDialogFragment();
        fragment.showAllowingStateLoss(getChildFragmentManager(), TAG_PARAMETER_DIALOG,
                arguments.toBuilder()
                        .sex(getSex())
                        .build());
    }

    @Override
    public void onParameterSet(@NonNull DomanTestParameter testParameter) {
        getPresenter().selectTestParameter(testParameter);
    }

    @Nullable
    public DomanTestParameter getTestParameter() {
        return state == null ? null : state.getTestParameter();
    }

    @Override
    protected String getIntentionText() {
        return getContext().getString(R.string.no_doman_test_data);
    }

    @Override
    protected ChartPlotter getChartPlotter(@NonNull CombinedChart chart, @NonNull DomanChartState state) {
        return new TestChartPlotter(chart, state.getTestParameter(), state.getTestResults());
    }

    public abstract TestType getTestType();

    @Override
    protected String getXTitle() {
        return getString(R.string.doman_chart_x_title);
    }

    @Override
    protected String getYTitle() {
        return getString(R.string.doman_chart_y_title);
    }
}
