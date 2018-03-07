package ru.android.healthvector.presentation.calendar.adapters.calendar;

import android.content.Context;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import lombok.Getter;

public class MonthViewAdapter extends CalendarViewAdapter {
    @Getter
    private int month, year;

    public MonthViewAdapter(Context context, OnSelectedDateChanged onSelectedDateChanged) {
        super(context, onSelectedDateChanged);
    }

    @Override
    protected void selectedDateChanged() {
        int year = selectedDate.getYear();
        int month = selectedDate.getMonthOfYear();
        if (this.year != year || this.month != month) {
            initCalendar(selectedDate);
        }
    }

    @Override
    protected void initCalendar(@NonNull LocalDate date) {
        super.initCalendar(date);
        this.year = date.getYear();
        this.month = date.getMonthOfYear();
        dates.clear();

        // отображаем календарь на 6 строчках
        // в случае, когда месяц занимает минимально возможное количество строчек, равное 4,
        // располагаем месяц "посередине" для красоты
        int daysInCurrentMonth = date.dayOfMonth().getMaximumValue();
        LocalDate firstDayOfMonth = new LocalDate(year, month, 1);
        int trailing = indexOfDayOfWeek(firstDayOfMonth);
        if (daysInCurrentMonth == 28 && trailing == 0) {
            // февраль 2021 года
            trailing = DAYS_IN_WEEK;
        }
        LocalDate current = firstDayOfMonth.minusDays(trailing);
        for (int i = 0; i < DAYS_IN_WEEK * 6; ++i) {
            dates.add(current);
            current = current.plusDays(1);
        }
    }

    @Override
    public void moveLeft() {
        LocalDate date = selectedDate.minusMonths(1).withDayOfMonth(1);
        setSelectedDate(date);
    }

    @Override
    public void moveRight() {
        LocalDate date = selectedDate.plusMonths(1).withDayOfMonth(1);
        setSelectedDate(date);
    }
}
