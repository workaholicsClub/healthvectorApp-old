package ru.android.childdiary.presentation.medical.filter.adapters;

import android.content.Context;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.R;
import ru.android.childdiary.utils.DateUtils;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@Builder
public class DateIntervalChips implements Chips {
    @Nullable
    LocalDate fromDate;
    @Nullable
    LocalDate toDate;

    @Override
    @Nullable
    public String getText(Context context) {
        if (fromDate == null && toDate == null) {
            return null;
        }
        if (fromDate != null && toDate == null) {
            String fromDateStr = DateUtils.date(context, fromDate);
            return context.getString(R.string.chips_from_date, fromDateStr);
        }
        if (fromDate == null) {
            String toDateStr = DateUtils.date(context, toDate);
            return context.getString(R.string.chips_to_date, toDateStr);
        }
        String fromDateStr = DateUtils.date(context, fromDate);
        String toDateStr = DateUtils.date(context, toDate);
        return context.getString(R.string.chips_from_date_to_date, fromDateStr, toDateStr);
    }

    @Override
    public boolean sameAs(Chips other) {
        if (!(other instanceof DateIntervalChips)) {
            return false;
        }
        DateIntervalChips dateIntervalChips = (DateIntervalChips) other;
        return ObjectUtils.equals(fromDate, dateIntervalChips.fromDate)
                && ObjectUtils.equals(toDate, dateIntervalChips.toDate);
    }
}
