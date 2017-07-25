package ru.android.childdiary.presentation.chart.antropometry.core;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.Px;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import lombok.Getter;
import lombok.NonNull;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.development.antropometry.AntropometryPoint;
import ru.android.childdiary.presentation.chart.core.LineEntry;

public class AntropometryChartDataMapper {
    private final Context context;
    @Getter
    private final CombinedData data = new CombinedData();
    private List<AntropometryPoint> values, lowValues, highValues;
    private LocalDate birthday;

    @ColorInt
    private final int lineColor, lineColorLow, lineColorHigh;
    @Px
    private final int lineWidth;

    @Inject
    public AntropometryChartDataMapper(Context context) {
        this.context = context;
        lineWidth = context.getResources().getDimensionPixelSize(R.dimen.line_width);
        lineColor = ContextCompat.getColor(context, R.color.line_color);
        lineColorLow = ContextCompat.getColor(context, R.color.line_color_low);
        lineColorHigh = ContextCompat.getColor(context, R.color.line_color_high);
    }

    public void calculateData(
            @NonNull List<AntropometryPoint> values,
            @NonNull List<AntropometryPoint> lowValues,
            @NonNull List<AntropometryPoint> highValues,
            @NonNull LocalDate birthday) {
        this.values = values;
        this.lowValues = lowValues;
        this.highValues = highValues;
        this.birthday = birthday;
        data.setData(generateLineData());
    }

    private LineData generateLineData() {
        LineData lineData = new LineData();
        if (lowValues.size() > 0) {
            lineData.addDataSet(generateLineDataSet(lowValues, lineColorLow, false,
                    AntropometryLineEntrySimple::new));
        }
        if (highValues.size() > 0) {
            lineData.addDataSet(generateLineDataSet(highValues, lineColorHigh, false,
                    AntropometryLineEntrySimple::new));
        }
        lineData.addDataSet(generateLineDataSet(values, lineColor, true,
                (xVal, yVal, point) -> new AntropometryLineEntry(xVal, yVal,
                        AntropometryLineEntryUtils.getIcon(context, false),
                        AntropometryLineEntryUtils.getIcon(context, true),
                        point)));
        return lineData;
    }

    private LineDataSet generateLineDataSet(@NonNull List<AntropometryPoint> values,
                                            @ColorInt int lineColor,
                                            boolean selectionEnabled,
                                            @NonNull LineEntryCreator creator) {
        List<Entry> lineEntries = new ArrayList<>();
        for (int i = 0; i < values.size(); ++i) {
            AntropometryPoint point = values.get(i);
            float yVal = (float) point.getValue().doubleValue();
            int xVal = Days.daysBetween(birthday, point.getDate()).getDays();
            Entry lineEntry = creator.create(xVal, yVal, point);
            lineEntries.add(lineEntry);
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntries, null);
        lineDataSet.setColor(lineColor);
        lineDataSet.setLineWidth(lineWidth);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setHighlightEnabled(selectionEnabled);
        boolean drawCircles = values.size() == 1;
        lineDataSet.setDrawCircles(drawCircles);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setCircleColor(lineColor);

        return lineDataSet;
    }

    private interface LineEntryCreator {
        LineEntry<AntropometryLineEntryInfo> create(int xVal, float yVal, @NonNull AntropometryPoint point);
    }
}
