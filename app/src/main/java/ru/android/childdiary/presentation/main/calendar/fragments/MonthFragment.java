package ru.android.childdiary.presentation.main.calendar.fragments;

import android.content.Context;

import ru.android.childdiary.R;
import ru.android.childdiary.presentation.main.calendar.adapters.MonthViewAdapter;
import ru.android.childdiary.utils.DateUtils;

public class MonthFragment extends CalendarFragment<MonthViewAdapter> {
    @Override
    protected MonthViewAdapter getCalendarViewAdapter() {
        return new MonthViewAdapter(getActivity(), this);
    }

    @Override
    protected void updateTitle(MonthViewAdapter adapter) {
        Context context = adapter.getContext();
        int month = adapter.getMonth();
        int year = adapter.getYear();
        String monthString = DateUtils.monthNominativeName(context, month);
        String text = context.getString(R.string.calendar_month_title, monthString, year);
        calendarTitle.setText(text);
    }
}
