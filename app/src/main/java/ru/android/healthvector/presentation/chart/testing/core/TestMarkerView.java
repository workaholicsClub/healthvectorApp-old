package ru.android.healthvector.presentation.chart.testing.core;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import butterknife.ButterKnife;
import ru.android.healthvector.R;
import ru.android.healthvector.domain.development.testing.data.processors.core.DomanResult;
import ru.android.healthvector.utils.strings.DateUtils;
import ru.android.healthvector.utils.strings.DoubleUtils;

class TestMarkerView extends MarkerView {
    private TextView textView;

    public TestMarkerView(Context context) {
        super(context, R.layout.marker_view);
        textView = ButterKnife.findById(this, R.id.textView);
    }

    @Override
    public void refreshContent(Entry entry, Highlight highlight) {
        if (!(entry instanceof TestLineEntry)) {
            super.refreshContent(entry, highlight);
            return;
        }
        DomanResult result = ((TestLineEntry) entry).getResult();
        String doubleStr = DoubleUtils.submultipleUnitFormat(result.getPercents());
        String valueStr = getContext().getString(R.string.percent_string_format, doubleStr);
        String dateStr = DateUtils.date(getContext(), result.getDate());
        String text = getContext().getString(R.string.two_values, valueStr, dateStr);
        textView.setText(text);
        super.refreshContent(entry, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-getWidth() / 2, -getHeight());
    }
}
