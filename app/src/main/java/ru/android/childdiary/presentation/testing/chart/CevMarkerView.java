package ru.android.childdiary.presentation.testing.chart;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import butterknife.ButterKnife;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanResult;
import ru.android.childdiary.utils.strings.DateUtils;
import ru.android.childdiary.utils.strings.DoubleUtils;

class CevMarkerView extends MarkerView {
    private TextView textView;

    public CevMarkerView(Context context) {
        super(context, R.layout.marker_view);
        textView = ButterKnife.findById(this, R.id.textView);
    }

    @Override
    public void refreshContent(Entry entry, Highlight highlight) {
        if (!(entry instanceof LineEntry)) {
            super.refreshContent(entry, highlight);
            return;
        }
        DomanResult result = ((LineEntry) entry).getResult();
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
