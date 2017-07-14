package ru.android.childdiary.presentation.testing.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.widgets.FieldJustifiedTextView;
import ru.android.childdiary.presentation.testing.TestingController;
import ru.android.childdiary.utils.strings.TestUtils;
import ru.android.childdiary.utils.ui.FontUtils;

public class TestingFinishFragment extends AppPartitionFragment {
    @BindView(R.id.descriptionView)
    FieldJustifiedTextView justifiedTextView;

    @BindView(R.id.chart)
    CombinedChart chart;

    @Nullable
    private TestingController testingController;
    private String text;
    @Nullable
    private DomanTestParameter parameter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_testing_finish;
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
        parameter = arguments.getParameter();
    }

    @Override
    protected void setupUi() {
        if (parameter != null) {
            ((BaseMvpActivity) getActivity()).setupToolbarTitle(TestUtils.toString(getContext(), parameter));
            setupChart();
        } else {
            chart.setVisibility(View.GONE);
        }
        justifiedTextView.setText(text);
    }

    private void setupChart() {
        chart.setDrawGridBackground(true);
        chart.setGridBackgroundColor(ContextCompat.getColor(getContext(), R.color.current_day_background));
        chart.getDescription().setEnabled(false);

        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });

        Legend l = chart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(FontUtils.getTypefaceRegular(getContext()));
        xAxis.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_small));
        xAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxisLeft.setTypeface(FontUtils.getTypefaceRegular(getContext()));
        yAxisLeft.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_medium));
        yAxisLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.placeholder_text));
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setValueFormatter((float value, AxisBase axis) -> getString(R.string.percent_format, value));

        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);


        CombinedData data = new CombinedData();

        data.addDataSet(generateLineData());
        data.addDataSet(generateLineData1());

        xAxis.setAxisMaximum(data.getXMax() + 1f);
        yAxisLeft.setAxisMaximum(data.getYMax() + 1f);

        chart.setData(data);

        chart.invalidate();
    }

    private LineDataSet generateLineData() {
        List<Entry> lineEntries = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            lineEntries.add(new Entry(i, i, ContextCompat.getDrawable(getContext(), R.drawable.dot)));
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Label");
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(Color.RED);
        lineDataSet.setColor(ContextCompat.getColor(getContext(), R.color.chart_line_normal_color));
        lineDataSet.setDrawValues(false);

        return lineDataSet;// new LineData(lineDataSet);
    }

    private LineDataSet generateLineData1() {
        List<Entry> lineEntries = new ArrayList<>();
        for (int i = 10; i < 20; ++i) {
            lineEntries.add(new Entry(i, -i, ContextCompat.getDrawable(getContext(), R.drawable.dot)));
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Label");
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(Color.RED);
        lineDataSet.setColor(ContextCompat.getColor(getContext(), R.color.chart_line_normal_color));
        lineDataSet.setDrawValues(false);

        return lineDataSet;//new LineData(lineDataSet);
    }
}
