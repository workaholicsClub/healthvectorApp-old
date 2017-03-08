package ru.android.childdiary.presentation.main.calendar.fragments;

import android.content.Context;

import org.joda.time.LocalDate;

import ru.android.childdiary.R;
import ru.android.childdiary.presentation.main.calendar.adapters.WeekViewAdapter;
import ru.android.childdiary.utils.DateUtils;

public class WeekFragment extends CalendarFragment<WeekViewAdapter> {
    @Override
    protected WeekViewAdapter getCalendarViewAdapter() {
        return new WeekViewAdapter(getActivity(), this);
    }

    @Override
    protected void updateTitle(WeekViewAdapter adapter) {
        Context context = adapter.getContext();
        LocalDate firstDayOfWeek = adapter.getFirstDayOfWeek();
        LocalDate lastDayOfWeek = adapter.getLastDayOfWeek();
        String text;
        if (firstDayOfWeek.getMonthOfYear() == lastDayOfWeek.getMonthOfYear()) {
            String monthString = DateUtils.monthGenitiveName(context, firstDayOfWeek.getMonthOfYear());
            text = context.getString(R.string.calendar_week_title_one_month,
                    firstDayOfWeek.getDayOfMonth(), lastDayOfWeek.getDayOfMonth(), monthString);
        } else {
            String firstDayMonthString = DateUtils.monthGenitiveName(context, firstDayOfWeek.getMonthOfYear());
            String lastDayMonthString = DateUtils.monthGenitiveName(context, lastDayOfWeek.getMonthOfYear());
            text = context.getString(R.string.calendar_week_title_two_month,
                    firstDayOfWeek.getDayOfMonth(), firstDayMonthString,
                    lastDayOfWeek.getDayOfMonth(), lastDayMonthString);
        }
        calendarTitle.setText(text);
    }
}
