package ru.android.healthvector.presentation.calendar.adapters.calendar;

import android.content.Context;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import lombok.Getter;

public class WeekViewAdapter extends CalendarViewAdapter {
    @Getter
    private LocalDate firstDayOfWeek, lastDayOfWeek;

    public WeekViewAdapter(Context context, OnSelectedDateChanged onSelectedDateChanged) {
        super(context, onSelectedDateChanged);
    }

    @Override
    protected void selectedDateChanged() {
        if (selectedDate.isBefore(firstDayOfWeek) || selectedDate.isAfter(lastDayOfWeek)) {
            initCalendar(selectedDate);
        }
    }

    @Override
    protected void initCalendar(@NonNull LocalDate date) {
        super.initCalendar(date);
        dates.clear();

        int trailing = indexOfDayOfWeek(date);
        firstDayOfWeek = date.minusDays(trailing);
        lastDayOfWeek = firstDayOfWeek.plusDays(DAYS_IN_WEEK - 1);
        LocalDate current = firstDayOfWeek;
        for (int i = 0; i < DAYS_IN_WEEK; ++i) {
            dates.add(current);
            current = current.plusDays(1);
        }
    }

    @Override
    public void moveLeft() {
        LocalDate date = firstDayOfWeek.minusWeeks(1);
        setSelectedDate(date);
    }

    @Override
    public void moveRight() {
        LocalDate date = firstDayOfWeek.plusWeeks(1);
        setSelectedDate(date);
    }
}
