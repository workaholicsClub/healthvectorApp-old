package ru.android.childdiary.presentation.main.calendar.adapters;

import android.content.Context;

import org.joda.time.LocalDate;

public class WeekViewAdapter extends CalendarViewAdapter {
    public WeekViewAdapter(Context context) {
        super(context);
    }

    @Override
    protected void initCalendar(LocalDate date) {
        dates.clear();

        int trailing = indexOfDayOfWeek(date);
        LocalDate current = date.minusDays(trailing);
        for (int i = 0; i < DAYS_IN_WEEK; ++i) {
            dates.add(current);
            current = current.plusDays(1);
        }
    }
}
