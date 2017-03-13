package ru.android.childdiary.presentation.main.calendar.fragments;

import android.content.Context;
import android.support.annotation.LayoutRes;

import org.joda.time.LocalDate;

import ru.android.childdiary.R;
import ru.android.childdiary.presentation.main.calendar.adapters.DayViewAdapter;
import ru.android.childdiary.utils.DateUtils;

public class DayFragment extends CalendarFragment<DayViewAdapter> {
    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_day;
    }

    @Override
    protected DayViewAdapter getCalendarViewAdapter() {
        return new DayViewAdapter(getContext(), this);
    }

    @Override
    protected int getGridViewHeight() {
        return getResources().getDimensionPixelSize(R.dimen.week_calendar_grid_view_height);
    }

    @Override
    protected String getCalendarTitleText(DayViewAdapter adapter) {
        Context context = getContext();
        LocalDate selectedDate = adapter.getSelectedDate();
        int day = selectedDate.getDayOfMonth();
        String monthName = DateUtils.monthGenitiveName(context, selectedDate.getMonthOfYear());
        String text = context.getString(R.string.calendar_selected_date_format, day, monthName);
        return text;
    }
}
