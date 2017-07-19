package ru.android.childdiary.presentation.testing.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;

import java.util.Collections;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanResult;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.widgets.FieldJustifiedTextView;
import ru.android.childdiary.presentation.testing.TestingController;
import ru.android.childdiary.presentation.testing.chart.ChartPlotter;
import ru.android.childdiary.utils.HtmlUtils;
import ru.android.childdiary.utils.strings.TestUtils;

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
    @BindView(R.id.legendLines)
    View legendLinesView;

    @Nullable
    private TestingController testingController;

    private String text;
    private Test test;
    @Nullable
    private DomanTestParameter parameter;
    @Nullable
    private DomanResult result;

    @Override
    protected int getLayoutResourceId() {
        return parameter != null && result != null ? R.layout.fragment_testing_finish_chart : R.layout.fragment_testing_finish;
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
        TestingFinishArguments arguments = (TestingFinishArguments) getArguments().getSerializable(ExtraConstants.EXTRA_APP_PARTITION_ARGUMENTS);
        if (arguments == null) {
            logger.error("no arguments provided");
            return;
        }
        text = arguments.getText();
        test = arguments.getTest();
        parameter = arguments.getParameter();
        result = arguments.getResult();
    }

    @Override
    protected void setupUi() {
        ((BaseMvpActivity) getActivity()).setupToolbarTitle(TestUtils.getTestTitle(getContext(), test, parameter));
        if (justifiedTextView != null) {
            justifiedTextView.setText(text);
            justifiedTextView.setOnLinkClickListener(this);
        }
        if (textView != null) {
            textView.setText(HtmlUtils.fromHtml(text));
        }
        if (chart != null) {
            ChartPlotter plotter = new ChartPlotter(chart, parameter, Collections.singletonList(result));
            plotter.setup();
        }
        if (legendLinesView != null) {
            legendLinesView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLinkClick(String url) {
        startBrowser(getContext(), url);
    }

    private void startBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            logger.debug("starting web browser");
            startActivity(intent);
        } else {
            logger.error("not found app to open intent: " + intent);
            showToast(getString(R.string.browser_not_available));
        }
    }
}
