package ru.android.childdiary.presentation.chart.core;

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
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseMvpFragment;
import ru.android.childdiary.presentation.core.ExtraConstants;

public abstract class ChartFragment<S extends ChartState, P extends ChartPresenter> extends BaseMvpFragment {
    @BindView(R.id.chartWrapper)
    View chartWrapper;

    @BindView(R.id.chart)
    CombinedChart chart;

    @BindView(R.id.xTitleView)
    TextView xTitleView;

    @BindView(R.id.yTitleView)
    TextView yTitleView;

    @BindView(R.id.legend)
    View legendView;

    @BindView(R.id.textViewIntention)
    TextView textViewIntention;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Nullable
    @Getter(AccessLevel.PROTECTED)
    private Sex sex;
    @Getter(AccessLevel.PROTECTED)
    private Child child;

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);
        child = (Child) getArguments().getSerializable(ExtraConstants.EXTRA_CHILD);
        if (child == null) {
            logger.error("no child provided");
            return;
        }
        sex = child.getSex();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        chartWrapper.setVisibility(View.GONE);
        legendView.setVisibility(View.GONE);
        textViewIntention.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        textViewIntention.setText(getIntentionText());
        xTitleView.setText(getXTitle());
        yTitleView.setText(getYTitle());
    }

    protected final void plotResults(@NonNull S state) {
        if (state.isEmpty()) {
            chartWrapper.setVisibility(View.GONE);
            legendView.setVisibility(View.GONE);
            textViewIntention.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        } else {
            chartWrapper.setVisibility(View.VISIBLE);
            legendView.setVisibility(View.VISIBLE);
            textViewIntention.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            ChartPlotter plotter = getChartPlotter(chart, state);
            plotter.setup();
        }
    }

    protected abstract P getPresenter();

    protected abstract String getIntentionText();

    protected abstract ChartPlotter getChartPlotter(@NonNull CombinedChart chart, @NonNull S state);

    protected abstract String getXTitle();

    protected abstract String getYTitle();
}