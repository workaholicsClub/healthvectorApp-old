package ru.android.childdiary.presentation.chart.antropometry.core;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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
import ru.android.childdiary.domain.interactors.development.antropometry.AntropometryPoint;
import ru.android.childdiary.presentation.chart.core.ChartPlotter;
import ru.android.childdiary.presentation.chart.core.ValueFormatter;
import ru.android.childdiary.utils.ui.FontUtils;

public class AntropometryChartPlotter implements ChartPlotter {
    /**
     * Размер текста меток на осях в пикселях.
     * Должен совпадать с R.dimen.text_size_small.
     */
    private static final int LABEL_TEXT_SIZE = 12;

    private final Logger logger = LoggerFactory.getLogger(toString());
    private final Context context;
    private final CombinedChart chart;
    private final List<AntropometryPoint> values, lowValues, highValues;
    private final ValueFormatter valueFormatter;

    private final int margin, lineWidth;

    private AntropometryLineEntry selectedEntry;

    public AntropometryChartPlotter(@NonNull CombinedChart chart,
                                    @NonNull List<AntropometryPoint> values,
                                    @NonNull List<AntropometryPoint> lowValues,
                                    @NonNull List<AntropometryPoint> highValues,
                                    @NonNull ValueFormatter valueFormatter) {
        context = chart.getContext();
        this.chart = chart;
        this.values = values;
        this.lowValues = lowValues;
        this.highValues = highValues;
        this.valueFormatter = valueFormatter;
        margin = context.getResources().getDimensionPixelSize(R.dimen.base_margin);
        lineWidth = context.getResources().getDimensionPixelSize(R.dimen.line_width);
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
        chart.setExtraOffsets(0, margin, 0, margin);
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR,
                CombinedChart.DrawOrder.LINE,
        });
        chart.getLegend().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
    }

    private void setupMarker() {
        MarkerView markerView = new AntropometryMarkerView(context, valueFormatter);
        markerView.setChartView(chart);
        chart.setMarker(markerView);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                if (!(entry instanceof AntropometryLineEntry)) {
                    return;
                }
                if (selectedEntry != null) {
                    logger.debug("deselect " + selectedEntry);
                    selectedEntry.deselect();
                }
                logger.debug("select " + entry);
                selectedEntry = (AntropometryLineEntry) entry;
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
                    return ""; // TODO даты
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
                (float value, AxisBase axis) -> valueFormatter.format(context, (double) value));
        yAxisLeft.setGranularity(1);
        yAxisLeft.setGranularityEnabled(true);
        yAxisLeft.setSpaceBottom(10);
        yAxisLeft.setSpaceTop(10);
    }

    private void setupData() {
        CombinedData data = new CombinedData();

        if (!lowValues.isEmpty()) {
            data.setData(generateLineData(lowValues));
        }
        data.setData(generateLineData(values));
        if (!highValues.isEmpty()) {
            data.setData(generateLineData(highValues));
        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMinimum(data.getXMin() - 1);
        xAxis.setAxisMaximum(data.getXMax() + 1);

        chart.setData(data);

        chart.resetZoom();
        chart.invalidate();
    }

    private LineData generateLineData(@NonNull List<AntropometryPoint> values) {
        List<Entry> lineEntries = new ArrayList<>();
        for (int i = 0; i < values.size(); ++i) {
            AntropometryPoint point = values.get(i);
            float yVal = (float) point.getValue().doubleValue();
            Entry lineEntry = new AntropometryLineEntry(i, yVal,
                    AntropometryLineEntryUtils.getIcon(context, false),
                    AntropometryLineEntryUtils.getIcon(context, true),
                    point);
            lineEntries.add(lineEntry);
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntries, null);
        lineDataSet.setColor(ContextCompat.getColor(context, R.color.line_color));
        lineDataSet.setLineWidth(lineWidth);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setHighlightEnabled(true);

        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        return lineData;
    }
}
