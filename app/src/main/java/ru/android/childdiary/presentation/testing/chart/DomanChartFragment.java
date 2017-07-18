package ru.android.childdiary.presentation.testing.chart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.CombinedChart;

import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import lombok.AccessLevel;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanResult;
import ru.android.childdiary.presentation.core.BaseMvpFragment;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.testing.fragments.ChartPlotter;

public abstract class DomanChartFragment extends BaseMvpFragment implements DomanChartView {
    @BindView(R.id.chart)
    CombinedChart chart;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Nullable
    @Getter(AccessLevel.PROTECTED)
    private Sex sex;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_doman_chart;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Child child = (Child) getArguments().getSerializable(ExtraConstants.EXTRA_CHILD);
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
        chart.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showResults(@NonNull LinkedHashMap<DomanTestParameter, List<DomanResult>> results) {
        if (results.isEmpty()) {
            // TODO no data
            return;
        }
        ChartPlotter chartPlotter = new ChartPlotter(chart, results);
        chartPlotter.setup();
        progressBar.setVisibility(View.GONE);
        chart.setVisibility(View.VISIBLE);
    }

    protected abstract DomanChartPresenter getPresenter();

    protected abstract TestType getTestType();
}
