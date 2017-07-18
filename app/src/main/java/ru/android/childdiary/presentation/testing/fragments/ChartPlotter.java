package ru.android.childdiary.presentation.testing.fragments;

import android.content.Context;
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

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import lombok.NonNull;
import lombok.val;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanResult;
import ru.android.childdiary.utils.strings.DateUtils;
import ru.android.childdiary.utils.ui.FontUtils;

public class ChartPlotter {
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
    private final LinkedHashMap<DomanTestParameter, List<DomanResult>> results;
    private final List<LocalDate> dates = new ArrayList<>();

    private final int[] stakedBarColors;
    private final int margin;

    private Entry selectedEntry;

    public ChartPlotter(@NonNull CombinedChart chart, @NonNull LinkedHashMap<DomanTestParameter, List<DomanResult>> results) {
        context = chart.getContext();
        this.chart = chart;
        this.results = results;
        stakedBarColors = new int[]{
                ContextCompat.getColor(context, R.color.slow_color),
                ContextCompat.getColor(context, R.color.normal_color),
                ContextCompat.getColor(context, R.color.advanced_color)
        };
        margin = context.getResources().getDimensionPixelSize(R.dimen.base_margin);
    }

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
        chart.setViewPortOffsets(margin, margin, margin, margin + margin);
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR,
                CombinedChart.DrawOrder.LINE,
        });
        chart.getLegend().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
    }

    private void setupMarker() {
        MarkerView markerView = new CevMarkerView(context);
        markerView.setChartView(chart);
        chart.setMarker(markerView);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                if (selectedEntry != null) {
                    logger.debug("unselect " + selectedEntry);
                    selectedEntry.setIcon(ContextCompat.getDrawable(context, R.drawable.dot_normal));
                }
                logger.debug("select " + entry);
                selectedEntry = entry;
                selectedEntry.setIcon(ContextCompat.getDrawable(context, R.drawable.dot_selected));
            }

            @Override
            public void onNothingSelected() {
                if (selectedEntry != null) {
                    logger.debug("deselect " + selectedEntry);
                    selectedEntry.setIcon(ContextCompat.getDrawable(context, R.drawable.dot_normal));
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
        xAxis.setTextColor(ContextCompat.getColor(context, R.color.black));
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(
                (float value, AxisBase axis) -> {
                    int index = (int) value;
                    return index >= 0 && index < dates.size()
                            ? DateUtils.date(context, dates.get(index))
                            : "";
                });
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setAvoidFirstLastClipping(true);
    }

    private void setupYAxis() {
        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
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
        CombinedData data = new CombinedData();

        data.setData(generateLineData());
        data.setData(generateBarData());

        XAxis xAxis = chart.getXAxis();
        if (dates.size() > 1) {
            xAxis.setAxisMinimum(data.getXMin() - 2);
            xAxis.setEnabled(false);
        } else {
            xAxis.setAxisMinimum(data.getXMin() - 1);
            xAxis.setAxisMaximum(data.getXMax() + 1);
        }

        chart.setData(data);

        chart.resetZoom();
        chart.invalidate();
    }

    private LineData generateLineData() {
        dates.clear();
        LineData lineData = new LineData();
        for (val entry : results.entrySet()) {
            List<DomanResult> domanResults = entry.getValue();
            if (domanResults.isEmpty()) {
                continue;
            }

            List<Entry> lineEntries = new ArrayList<>();
            for (int i = 0; i < domanResults.size(); ++i) {
                DomanResult result = domanResults.get(i);
                float yVal = (float) result.getPercents().doubleValue();
                Entry lineEntry = new Entry(i, yVal, ContextCompat.getDrawable(context, R.drawable.dot_normal), result);
                lineEntries.add(lineEntry);
                if (!dates.contains(result.getDate())) {
                    dates.add(result.getDate());
                }
            }

            LineDataSet lineDataSet = new LineDataSet(lineEntries, null);
            lineDataSet.setColor(ContextCompat.getColor(context, R.color.chart_line_normal_color));
            lineDataSet.setDrawValues(false);
            lineDataSet.setDrawHighlightIndicators(false);
            lineDataSet.setHighlightEnabled(true);

            lineData.addDataSet(lineDataSet);
        }
        Collections.sort(dates);
        return lineData;
    }

    private BarData generateBarData() {
        BarData barData = new BarData();
        int count = dates.size() > 1 ? dates.size() + 1 : 1;
        for (int i = 0; i < count; ++i) {
            List<BarEntry> barEntries = new ArrayList<>();

            BarEntry barEntry = new BarEntry(i, STACKED_BAR_PARTS);

            barEntries.add(barEntry);

            BarDataSet barDataSet = new BarDataSet(barEntries, null);
            barDataSet.setDrawValues(false);
            barDataSet.setColors(stakedBarColors);
            barDataSet.setHighLightAlpha(0);
            barDataSet.setHighlightEnabled(false);

            barData.addDataSet(barDataSet);
        }

        if (count > 1) {
            barData.groupBars(-0.5f, 0, 0);
        }
        return barData;
    }
}
