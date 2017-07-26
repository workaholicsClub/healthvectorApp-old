package ru.android.childdiary.presentation.chart.testing.core;

import android.content.Context;
import android.support.annotation.Px;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanResult;
import ru.android.childdiary.presentation.chart.core.ChartPlotter;
import ru.android.childdiary.utils.ui.FontUtils;

public class TestChartPlotter implements ChartPlotter {
    private static final int LINE_WIDTH_IN_DP = 2;

    /**
     * Размер текста меток на осях в пикселях.
     * Должен совпадать с R.dimen.text_size_small.
     */
    private static final int LABEL_TEXT_SIZE = 12;

    private static final float[] STACKED_BAR_PARTS = new float[]{
            (float) DomanResult.SLOW, (float) DomanResult.NORMAL, (float) DomanResult.ADVANCED
    };

    private final Logger logger = LoggerFactory.getLogger(toString());
    private final Context context;
    private final CombinedChart chart;
    private final DomanTestParameter testParameter;
    private final List<DomanResult> results;

    private final int[] stakedBarColors;
    @Px
    private final int margin;

    private TestLineEntry selectedEntry;

    public TestChartPlotter(@NonNull CombinedChart chart,
                            @NonNull DomanTestParameter testParameter,
                            @NonNull List<DomanResult> results) {
        context = chart.getContext();
        this.chart = chart;
        this.testParameter = testParameter;
        this.results = results;
        stakedBarColors = new int[]{
                ContextCompat.getColor(context, R.color.slow_color),
                ContextCompat.getColor(context, R.color.normal_color),
                ContextCompat.getColor(context, R.color.advanced_color)
        };
        margin = context.getResources().getDimensionPixelSize(R.dimen.base_margin);
    }

    @Override
    public void setup() {
        setupChart();
        setupMarker();
        setupXAxis();
        setupYAxis();
        setupData();
    }

    private void setupChart() {
        chart.setDrawGridBackground(true);
        chart.setBackgroundResource(R.color.current_day_background);
        chart.setGridBackgroundColor(ContextCompat.getColor(context, R.color.current_day_background));
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);
        chart.setHighlightPerTapEnabled(true);
        chart.setHighlightPerDragEnabled(false);
        chart.setMaxHighlightDistance(10);
        chart.setExtraOffsets(0, margin, 0, margin);
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR,
                CombinedChart.DrawOrder.LINE,
        });
        chart.getLegend().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.setNoDataText("");
    }

    private void setupMarker() {
        MarkerView markerView = new TestMarkerView(context);
        markerView.setChartView(chart);
        chart.setMarker(markerView);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                if (!(entry instanceof TestLineEntry)) {
                    return;
                }
                if (selectedEntry != null) {
                    logger.debug("deselect " + selectedEntry);
                    selectedEntry.deselect();
                }
                logger.debug("select " + entry);
                selectedEntry = (TestLineEntry) entry;
                selectedEntry.select();
            }

            @Override
            public void onNothingSelected() {
                if (selectedEntry != null) {
                    logger.debug("deselect " + selectedEntry);
                    selectedEntry.deselect();
                    selectedEntry = null;
                }
            }
        });
    }

    private void setupXAxis() {
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(FontUtils.getTypefaceRegular(context));
        xAxis.setTextSize(LABEL_TEXT_SIZE);
        xAxis.setTextColor(ContextCompat.getColor(context, R.color.placeholder_text));
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(
                (float value, AxisBase axis) -> {
                    int index = (int) value;
                    return index >= 0 && index < results.size()
                            ? String.valueOf(results.get(index).getStage() + 1)
                            : "";
                });
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setAvoidFirstLastClipping(true);
    }

    private void setupYAxis() {
        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxisLeft.setTypeface(FontUtils.getTypefaceRegular(context));
        yAxisLeft.setTextSize(LABEL_TEXT_SIZE);
        yAxisLeft.setTextColor(ContextCompat.getColor(context, R.color.placeholder_text));
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setValueFormatter(
                (float value, AxisBase axis) -> context.getString(R.string.percent_double_format, value));
        yAxisLeft.setGranularity(1);
        yAxisLeft.setGranularityEnabled(true);
        yAxisLeft.setSpaceBottom(10);
        yAxisLeft.setSpaceTop(10);
    }

    private void setupData() {
        if (results.isEmpty()) {
            chart.clear();
        } else {
            CombinedData data = new CombinedData();

            data.setData(generateLineData());
            data.setData(generateBarData());

            XAxis xAxis = chart.getXAxis();
            xAxis.setAxisMinimum(data.getXMin() - 1);
            xAxis.setAxisMaximum(data.getXMax() + 1);

            chart.setData(data);
        }

        chart.resetZoom();
        chart.invalidate();
    }

    private LineData generateLineData() {
        List<Entry> lineEntries = new ArrayList<>();
        for (int i = 0; i < results.size(); ++i) {
            DomanResult result = results.get(i);
            float yVal = (float) result.getPercents().doubleValue();
            Entry lineEntry = new TestLineEntry(i, yVal,
                    TestLineEntryUtils.getIcon(context, testParameter, false),
                    TestLineEntryUtils.getIcon(context, testParameter, true),
                    testParameter, result);
            lineEntries.add(lineEntry);
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntries, null);
        lineDataSet.setColor(ContextCompat.getColor(context, R.color.line_color));
        lineDataSet.setLineWidth(LINE_WIDTH_IN_DP);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawCircleHole(false);

        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        return lineData;
    }

    private BarData generateBarData() {
        BarData barData = new BarData();
        for (int i = 0; i < results.size(); ++i) {
            BarEntry barEntry = new BarEntry(i, STACKED_BAR_PARTS);

            List<BarEntry> barEntries = new ArrayList<>();
            barEntries.add(barEntry);

            BarDataSet barDataSet = new BarDataSet(barEntries, null);
            barDataSet.setDrawValues(false);
            barDataSet.setColors(stakedBarColors);
            barDataSet.setHighLightAlpha(0);
            barDataSet.setHighlightEnabled(false);

            barData.addDataSet(barDataSet);
        }

        barData.setBarWidth(1);
        return barData;
    }
}
