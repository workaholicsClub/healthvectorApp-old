package ru.android.childdiary.presentation.main.calendar.fragments;

import ru.android.childdiary.presentation.main.calendar.adapters.CalendarViewAdapter;
import ru.android.childdiary.presentation.main.calendar.adapters.WeekViewAdapter;

public class WeekFragment extends CalendarFragment {
    @Override
    protected CalendarViewAdapter getCalendarViewAdapter() {
        return new WeekViewAdapter(getActivity());
    }
}
