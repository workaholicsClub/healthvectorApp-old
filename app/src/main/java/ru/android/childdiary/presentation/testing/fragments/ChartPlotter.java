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

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import ru.android.childdiary.R;
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

    private final Context context;
    private final CombinedChart chart;
    private final List<DomanResult> results;

    private final int[] stakedBarColors;
    private final int margin;

    private Entry selectedEntry;

    public ChartPlotter(@NonNull CombinedChart chart, @NonNull List<DomanResult> results) {
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
        chart.setDrawGridBackground(true);
        chart.setGridBackgroundColor(ContextCompat.getColor(context, R.color.current_day_background));
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);
        chart.setViewPortOffsets(margin, 0, margin, margin + margin);
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR,
                CombinedChart.DrawOrder.LINE,
        });
        chart.getLegend().setEnabled(false);

        MarkerView markerView = new CevMarkerView(context);
        markerView.setChartView(chart);
        chart.setMarker(markerView);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                selectedEntry = entry;
                selectedEntry.setIcon(ContextCompat.getDrawable(context, R.drawable.dot_selected));
            }

            @Override
            public void onNothingSelected() {
                if (selectedEntry != null) {
                    selectedEntry.setIcon(ContextCompat.getDrawable(context, R.drawable.dot_normal));
                    selectedEntry = null;
                }
            }
        });

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
                    return index >= 0 && index < results.size()
                            ? DateUtils.date(context, results.get(index).getDate())
                            : "";
                });
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setAvoidFirstLastClipping(true);

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

        chart.getAxisRight().setEnabled(false);

        CombinedData data = new CombinedData();

        data.setData(generateLineData());
        data.setData(generateBarData());

        xAxis.setAxisMaximum(data.getXMax() + 1);
        xAxis.setAxisMinimum(data.getXMin() - 1);

        chart.setData(data);

        chart.invalidate();
    }

    private LineData generateLineData() {
        List<Entry> lineEntries = new ArrayList<>();
        for (int i = 0; i < results.size(); ++i) {
            DomanResult result = results.get(i);
            float yVal = (float) result.getPercents().doubleValue();
            Entry entry = new Entry(i, yVal, ContextCompat.getDrawable(context, R.drawable.dot_normal), result);
            lineEntries.add(entry);
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntries, null);
        lineDataSet.setColor(ContextCompat.getColor(context, R.color.chart_line_normal_color));
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setHighlightEnabled(true);

        return new LineData(lineDataSet);
    }

    private BarData generateBarData() {
        BarData barData = new BarData();
        for (int i = 0; i < results.size(); ++i) {
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

        if (results.size() > 1) {
            barData.groupBars(0, 0, 0);
        }
        return barData;
    }
}
