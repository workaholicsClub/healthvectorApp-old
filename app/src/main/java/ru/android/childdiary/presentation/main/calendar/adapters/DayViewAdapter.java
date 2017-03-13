package ru.android.childdiary.presentation.main.calendar.adapters;

import android.content.Context;

import org.joda.time.LocalDate;

public class DayViewAdapter extends WeekViewAdapter {
    public DayViewAdapter(Context context, OnSelectedDateChanged onSelectedDateChanged) {
        super(context, onSelectedDateChanged);
    }

    @Override
    public void moveLeft() {
        LocalDate date = selectedDate.minusDays(1);
        setSelectedDate(date);
    }

    @Override
    public void moveRight() {
        LocalDate date = selectedDate.plusDays(1);
        setSelectedDate(date);
    }
}
