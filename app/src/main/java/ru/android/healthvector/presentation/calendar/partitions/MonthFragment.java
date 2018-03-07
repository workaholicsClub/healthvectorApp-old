package ru.android.healthvector.presentation.calendar.partitions;

import android.support.annotation.LayoutRes;

import ru.android.healthvector.R;
import ru.android.healthvector.presentation.calendar.adapters.calendar.MonthViewAdapter;
import ru.android.healthvector.utils.strings.DateUtils;

public class MonthFragment extends BaseCalendarFragment<MonthViewAdapter> {
    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_calendar_month;
    }

    @Override
    protected MonthViewAdapter createCalendarViewAdapter() {
        return new MonthViewAdapter(getContext(), this);
    }

    @Override
    protected int getGridViewHeight() {
        return getResources().getDimensionPixelSize(R.dimen.month_calendar_grid_view_height);
    }

    @Override
    protected String getCalendarTitleText(MonthViewAdapter adapter) {
        int month = adapter.getMonth();
        int year = adapter.getYear();
        String monthString = DateUtils.monthNominativeName(getContext(), month);
        String text = getString(R.string.calendar_month_title, monthString, year);
        return text;
    }

    @Override
    protected boolean showEventsHeaderNavigationButtons() {
        return false;
    }
}
