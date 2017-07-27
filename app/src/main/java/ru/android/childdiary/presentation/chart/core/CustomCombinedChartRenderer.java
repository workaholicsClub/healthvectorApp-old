package ru.android.childdiary.presentation.chart.core;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface;
import com.github.mikephil.charting.renderer.CombinedChartRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class CustomCombinedChartRenderer extends CombinedChartRenderer {
    public CustomCombinedChartRenderer(CombinedChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    @Override
    protected boolean isDrawingValuesAllowed(ChartInterface chart) {
        return true;
    }
}
