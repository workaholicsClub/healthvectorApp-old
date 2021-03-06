package ru.android.healthvector.presentation.testing.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.DomanTestParameter;
import ru.android.healthvector.domain.development.testing.data.processors.core.DomanResult;
import ru.android.healthvector.domain.development.testing.data.tests.core.Test;
import ru.android.healthvector.presentation.chart.testing.core.TestChartPlotter;
import ru.android.healthvector.presentation.core.AppPartitionFragment;
import ru.android.healthvector.presentation.core.BaseMvpActivity;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.core.fields.widgets.FieldJustifiedTextView;
import ru.android.healthvector.presentation.testing.TestingController;
import ru.android.healthvector.utils.HtmlUtils;
import ru.android.healthvector.utils.IntentUtils;
import ru.android.healthvector.utils.strings.TestUtils;
import ru.android.healthvector.utils.ui.ResourcesUtils;

public class TestingFinishFragment extends AppPartitionFragment implements HtmlUtils.OnLinkClickListener {
    @Nullable
    @BindView(R.id.descriptionView)
    FieldJustifiedTextView justifiedTextView;

    @Nullable
    @BindView(R.id.textView)
    TextView textView;

    @Nullable
    @BindView(R.id.chart)
    CombinedChart chart;

    @Nullable
    @BindView(R.id.xTitleView)
    TextView xTitleView;

    @Nullable
    @BindView(R.id.yTitleView)
    TextView yTitleView;

    @Nullable
    @BindView(R.id.legend)
    View legendView;

    @Nullable
    @BindView(R.id.textViewNoChartData)
    TextView textViewNoChartData;

    @BindView(R.id.buttonStopTesting)
    Button buttonStopTesting;

    @Nullable
    private TestingController testingController;

    private String text;
    private Test test;
    @Nullable
    private DomanTestParameter parameter;
    private List<DomanResult> results;
    private boolean isInTestMode;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return parameter != null ? R.layout.fragment_testing_finish_chart : R.layout.fragment_testing_finish;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TestingController) {
            testingController = (TestingController) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        testingController = null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        TestingFinishArguments arguments = getArguments() == null ? null
                : (TestingFinishArguments) getArguments().getSerializable(ExtraConstants.EXTRA_APP_PARTITION_ARGUMENTS);
        if (arguments == null) {
            logger.error("no arguments provided");
            return;
        }
        text = arguments.getText();
        test = arguments.getTest();
        parameter = arguments.getParameter();
        results = arguments.getResults();
        isInTestMode = arguments.isInTestMode();
    }

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
        if (justifiedTextView != null) {
            justifiedTextView.setText(text);
            justifiedTextView.setOnLinkClickListener(this);
        }
        if (textView != null) {
            textView.setText(HtmlUtils.fromHtml(text));
        }
        if (chart != null) {
            TestChartPlotter plotter = new TestChartPlotter(chart, parameter, results);
            plotter.setup();
        }
        if (xTitleView != null) {
            xTitleView.setText(getString(R.string.doman_chart_x_title));
            xTitleView.setVisibility(results.isEmpty() ? View.GONE : View.VISIBLE);
        }
        if (yTitleView != null) {
            yTitleView.setText(getString(R.string.doman_chart_y_title));
            yTitleView.setVisibility(results.isEmpty() ? View.GONE : View.VISIBLE);
        }
        if (textViewNoChartData != null) {
            textViewNoChartData.setVisibility(results.isEmpty() ? View.VISIBLE : View.GONE);
        }
        if (legendView != null) {
            legendView.setVisibility(results.isEmpty() ? View.INVISIBLE : View.VISIBLE);
        }
        buttonStopTesting.setVisibility(isInTestMode ? View.VISIBLE : View.GONE);
        if (getActivity() == null) {
            logger.error("activity is null");
            return;
        }
        ((BaseMvpActivity) getActivity()).setupToolbarTitle(TestUtils.getTestTitle(getContext(), test, parameter));
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        buttonStopTesting.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), true));
    }

    @OnClick(R.id.buttonStopTesting)
    void onButtonStartTestingClick() {
        if (testingController != null) {
            testingController.stopTesting();
        }
    }

    @Override
    public void onLinkClick(String url) {
        IntentUtils.startWebBrowser(getContext(), url);
    }
}
