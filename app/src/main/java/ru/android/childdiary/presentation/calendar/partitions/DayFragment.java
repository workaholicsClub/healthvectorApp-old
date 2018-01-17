package ru.android.childdiary.presentation.calendar.partitions;

import android.support.annotation.LayoutRes;

import org.joda.time.LocalDate;

import ru.android.childdiary.R;
import ru.android.childdiary.presentation.calendar.adapters.calendar.DayViewAdapter;
import ru.android.childdiary.utils.strings.DateUtils;

public class DayFragment extends BaseCalendarFragment<DayViewAdapter> {
    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_calendar_day;
    }

    @Override
    protected DayViewAdapter createCalendarViewAdapter() {
        return new DayViewAdapter(getContext(), this);
    }

    @Override
    protected int getGridViewHeight() {
        return getResources().getDimensionPixelSize(R.dimen.week_calendar_grid_view_height);
    }

    @Override
    protected String getCalendarTitleText(DayViewAdapter adapter) {
        LocalDate selectedDate = adapter.getSelectedDate();
        int day = selectedDate.getDayOfMonth();
        String monthName = DateUtils.monthGenitiveName(getContext(), selectedDate.getMonthOfYear());
        String text = getString(R.string.calendar_selected_date_format, day, monthName);
        return text;
    }

    @Override
    protected boolean showEventsHeaderNavigationButtons() {
        return true;
    }
}
