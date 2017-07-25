package ru.android.childdiary.presentation.chart.antropometry.core;

import android.graphics.Canvas;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class CustomXAxisRenderer extends XAxisRenderer {
    public CustomXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer transformer) {
        super(viewPortHandler, xAxis, transformer);
    }

    @Override
    protected void drawLabels(Canvas c, float pos, MPPointF anchor) {
        final int LABELS_COUNT = 3;

        float positions[] = new float[LABELS_COUNT * 2];

        float lowerBound = mViewPortHandler.contentLeft();
        float upperBound = mViewPortHandler.contentRight();
        float width = upperBound - lowerBound;

        for (int i = 0; i < LABELS_COUNT; ++i) {
            positions[i * 2] = lowerBound + width * ((float) i / ((float) LABELS_COUNT - 1f));
        }

        float values[] = new float[positions.length];
        System.arraycopy(positions, 0, values, 0, positions.length);

        mTrans.pixelsToValue(values);

        for (int i = 0; i < LABELS_COUNT * 2; i += 2) {
            float value = values[i];
            value = Math.round(value);
            String label = mXAxis.getValueFormatter().getFormattedValue(value, mXAxis);
            if (i == 0) {
                float textWidth = Utils.calcTextWidth(mAxisLabelPaint, label);
                positions[i] += textWidth / 2;
            } else if (i == LABELS_COUNT * 2 - 2) {
                float textWidth = Utils.calcTextWidth(mAxisLabelPaint, label);
                positions[i] -= textWidth / 2;
            }
            drawLabel(c, label, positions[i], pos, anchor, 0);
        }
    }
}
