package ru.android.healthvector.presentation.calendar.partitions;

import android.content.Context;
import android.support.annotation.LayoutRes;

import org.joda.time.LocalDate;

import ru.android.healthvector.R;
import ru.android.healthvector.presentation.calendar.adapters.calendar.WeekViewAdapter;
import ru.android.healthvector.utils.strings.DateUtils;

public class WeekFragment extends BaseCalendarFragment<WeekViewAdapter> {
    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_calendar_week;
    }

    @Override
    protected WeekViewAdapter createCalendarViewAdapter() {
        return new WeekViewAdapter(getContext(), this);
    }

    @Override
    protected int getGridViewHeight() {
        return getResources().getDimensionPixelSize(R.dimen.week_calendar_grid_view_height);
    }

    @Override
    protected String getCalendarTitleText(WeekViewAdapter adapter) {
        Context context = getContext();
        LocalDate firstDayOfWeek = adapter.getFirstDayOfWeek();
        LocalDate lastDayOfWeek = adapter.getLastDayOfWeek();
        String text;
        if (firstDayOfWeek.getMonthOfYear() == lastDayOfWeek.getMonthOfYear()) {
            String monthString = DateUtils.monthGenitiveName(context, firstDayOfWeek.getMonthOfYear());
            text = getString(R.string.calendar_week_title_one_month,
                    firstDayOfWeek.getDayOfMonth(), lastDayOfWeek.getDayOfMonth(), monthString);
        } else {
            String firstDayMonthString = DateUtils.monthGenitiveName(context, firstDayOfWeek.getMonthOfYear());
            String lastDayMonthString = DateUtils.monthGenitiveName(context, lastDayOfWeek.getMonthOfYear());
            text = getString(R.string.calendar_week_title_two_month,
                    firstDayOfWeek.getDayOfMonth(), firstDayMonthString,
                    lastDayOfWeek.getDayOfMonth(), lastDayMonthString);
        }
        return text;
    }

    @Override
    protected boolean showEventsHeaderNavigationButtons() {
        return false;
    }
}
