package ru.android.childdiary.presentation.chart.antropometry.core;

import android.content.Context;
import android.support.annotation.Px;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.NonNull;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.chart.core.ChartPlotter;
import ru.android.childdiary.presentation.chart.core.ValueFormatter;
import ru.android.childdiary.utils.strings.DateUtils;
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
    private final ValueFormatter valueFormatter;
    private final float yGranularity;
    private final LocalDate birthday;
    private final CombinedData data;

    @Px
    private final int margin;

    private AntropometryLineEntry selectedEntry;

    public AntropometryChartPlotter(@NonNull CombinedChart chart,
                                    @NonNull ValueFormatter valueFormatter,
                                    float yGranularity,
                                    @NonNull LocalDate birthday,
                                    @NonNull CombinedData data) {
        context = chart.getContext();
        this.chart = chart;
        this.valueFormatter = valueFormatter;
        this.yGranularity = yGranularity > 0 ? yGranularity : 1;
        this.birthday = birthday;
        this.data = data;
        margin = context.getResources().getDimensionPixelSize(R.dimen.base_margin);
    }

    @Override
    public void setup() {
        setupChart();
        setupMarker();
        setupXAxis();
        setupYAxis();
        setupData();
        // TODO выкинуть лишнее
        //chart.setVisibleXRange(0, 30);
        //chart.moveViewToX(0);
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
                    LocalDate date = birthday.plusDays(index);
                    return DateUtils.date(context, date);
                });
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setLabelCount(2);
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
        yAxisLeft.setGranularity(yGranularity);
        yAxisLeft.setGranularityEnabled(true);
        yAxisLeft.setSpaceBottom(10);
        yAxisLeft.setSpaceTop(10);
    }

    private void setupData() {
        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMinimum(data.getXMin() - 1);
        xAxis.setAxisMaximum(data.getXMax() + 1);

        chart.setData(data);

        chart.resetZoom();
        chart.invalidate();
    }
}
