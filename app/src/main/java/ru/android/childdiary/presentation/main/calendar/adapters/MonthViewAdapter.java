package ru.android.childdiary.presentation.main.calendar.adapters;

import android.content.Context;

import org.joda.time.LocalDate;

import lombok.Getter;

public class MonthViewAdapter extends CalendarViewAdapter {
    @Getter
    private int month, year;

    public MonthViewAdapter(Context context) {
        super(context);
    }

    @Override
    protected void selectedDateChanged() {
        int year = getSelectedDate().getYear();
        int month = getSelectedDate().getMonthOfYear();
        if (this.year != year || this.month != month) {
            initCalendar(getSelectedDate());
        }
    }

    @Override
    protected void initCalendar(LocalDate date) {
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
}
