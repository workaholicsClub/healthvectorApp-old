package ru.android.healthvector.presentation.chart.testing.pages.core;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.github.mikephil.charting.charts.CombinedChart;

import icepick.State;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.DomanTestParameter;
import ru.android.healthvector.data.types.TestType;
import ru.android.healthvector.presentation.chart.core.ChartFragment;
import ru.android.healthvector.presentation.chart.core.ChartPlotter;
import ru.android.healthvector.presentation.chart.testing.TestChartActivity;
import ru.android.healthvector.presentation.chart.testing.core.TestChartPlotter;
import ru.android.healthvector.presentation.chart.testing.dialogs.ParameterDialogArguments;
import ru.android.healthvector.presentation.chart.testing.dialogs.ParameterDialogFragment;
import ru.android.healthvector.utils.ui.ThemeUtils;

public abstract class DomanChartFragment extends ChartFragment<DomanChartState, DomanChartPresenter> implements DomanChartView,
        ParameterDialogFragment.Listener {
    private static final String TAG_PARAMETER_DIALOG = "TAG_PARAMETER_DIALOG";

    @State
    boolean needToShowWarning;

    private DomanChartState state;

    @Override
    @LayoutRes
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
        updateTitle();
        if (state.isInvalidResults()) {
            if (isSelected()) {
                warnAboutInvalidResults();
            } else {
                needToShowWarning = true;
            }
        }
    }

    private void warnAboutInvalidResults() {
        if (getContext() == null) {
            logger.error("context is null");
            return;
        }
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.birthday_changed)
                .setPositiveButton(R.string.ok, null)
                .show();
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

    @Override
    protected String getIntentionText() {
        return getString(R.string.no_doman_test_data);
    }

    @Override
    protected ChartPlotter getChartPlotter(@NonNull CombinedChart chart, @NonNull DomanChartState state) {
        return new TestChartPlotter(chart, state.getTestParameter(), state.getFilteredTestResults());
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

    @Override
    public void setSelected() {
        updateTitle();
        if (needToShowWarning) {
            warnAboutInvalidResults();
            needToShowWarning = false;
        }
    }

    private void updateTitle() {
        if (getActivity() == null) {
            logger.error("activity is null");
            return;
        }
        ((TestChartActivity) getActivity()).updateTitle(state.getTestType(), state.getTestParameter());
    }
}
