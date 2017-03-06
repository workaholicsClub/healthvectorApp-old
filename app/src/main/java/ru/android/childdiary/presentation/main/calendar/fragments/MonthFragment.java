package ru.android.childdiary.presentation.main.calendar.fragments;

import ru.android.childdiary.presentation.main.calendar.adapters.CalendarViewAdapter;
import ru.android.childdiary.presentation.main.calendar.adapters.MonthViewAdapter;

public class MonthFragment extends CalendarFragment {
    @Override
    protected CalendarViewAdapter getCalendarViewAdapter() {
        return new MonthViewAdapter(getActivity());
    }
}
