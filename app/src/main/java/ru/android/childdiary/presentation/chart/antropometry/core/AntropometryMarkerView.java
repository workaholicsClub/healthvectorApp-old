package ru.android.childdiary.presentation.chart.antropometry.core;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import butterknife.ButterKnife;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.development.antropometry.AntropometryPoint;
import ru.android.childdiary.presentation.chart.core.ValueFormatter;
import ru.android.childdiary.utils.strings.DateUtils;

class AntropometryMarkerView extends MarkerView {
    private final TextView textView;
    private final ValueFormatter valueFormatter;

    public AntropometryMarkerView(Context context, ValueFormatter valueFormatter) {
        super(context, R.layout.marker_view);
        textView = ButterKnife.findById(this, R.id.textView);
        this.valueFormatter = valueFormatter;
    }

    @Override
    public void refreshContent(Entry entry, Highlight highlight) {
        if (!(entry instanceof AntropometryLineEntry)) {
            super.refreshContent(entry, highlight);
            return;
        }
        AntropometryPoint point = ((AntropometryLineEntry) entry).getValuePoint();
        String valueStr = valueFormatter.format(getContext(), point.getValue());
        String dateStr = DateUtils.date(getContext(), point.getDate());
        String text = getContext().getString(R.string.two_values, valueStr, dateStr);
        textView.setText(text);
        super.refreshContent(entry, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-getWidth() / 2, -getHeight());
    }
}
