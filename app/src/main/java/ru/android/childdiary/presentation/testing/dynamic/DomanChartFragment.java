package ru.android.childdiary.presentation.testing.dynamic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;

import butterknife.BindView;
import lombok.AccessLevel;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseMvpFragment;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.testing.chart.ChartPlotter;
import ru.android.childdiary.presentation.testing.dialogs.ParameterDialogArguments;
import ru.android.childdiary.presentation.testing.dialogs.ParameterDialogFragment;

public abstract class DomanChartFragment extends BaseMvpFragment implements DomanChartView,
        ParameterDialogFragment.Listener {
    private static final String TAG_PARAMETER_DIALOG = "TAG_PARAMETER_DIALOG";

    @BindView(R.id.chartWrapper)
    View chartWrapper;

    @BindView(R.id.chart)
    CombinedChart chart;

    @BindView(R.id.legend)
    View legendView;

    @BindView(R.id.textViewIntention)
    TextView textViewIntention;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Nullable
    @Getter(AccessLevel.PROTECTED)
    private Sex sex;
    private Child child;
    private DomanChartState state;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_doman_chart;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);
        child = (Child) getArguments().getSerializable(ExtraConstants.EXTRA_CHILD);
        if (child == null) {
            logger.error("no child provided");
            return;
        }
        sex = child.getSex();
        if (savedInstanceState == null) {
            getPresenter().init(child, getTestType());
        }
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        chartWrapper.setVisibility(View.GONE);
        legendView.setVisibility(View.GONE);
        textViewIntention.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showResults(@NonNull DomanChartState state) {
        this.state = state;
        ((TestChartActivity) getActivity()).updateTitle(state.getTestType(), state.getTestParameter());
        if (state.getTestResults().isEmpty()) {
            chartWrapper.setVisibility(View.GONE);
            legendView.setVisibility(View.GONE);
            textViewIntention.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        } else {
            chartWrapper.setVisibility(View.VISIBLE);
            legendView.setVisibility(View.VISIBLE);
            textViewIntention.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            ChartPlotter plotter = new ChartPlotter(chart, state.getTestParameter(), state.getTestResults());
            plotter.setup();
        }
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

    protected abstract DomanChartPresenter getPresenter();

    public abstract TestType getTestType();

    @Nullable
    public DomanTestParameter getTestParameter() {
        return state == null ? null : state.getTestParameter();
    }
}
