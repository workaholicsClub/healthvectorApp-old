package ru.android.childdiary.presentation.calendar.partitions;

import android.content.Context;
import android.support.annotation.LayoutRes;

import ru.android.childdiary.R;
import ru.android.childdiary.presentation.calendar.adapters.calendar.MonthViewAdapter;
import ru.android.childdiary.utils.DateUtils;

public class MonthFragment extends BaseCalendarFragment<MonthViewAdapter> {
    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_calendar_month;
    }

    @Override
    protected MonthViewAdapter getCalendarViewAdapter() {
        return new MonthViewAdapter(getContext(), this);
    }

    @Override
    protected int getGridViewHeight() {
        return getResources().getDimensionPixelSize(R.dimen.month_calendar_grid_view_height);
    }

    @Override
    protected String getCalendarTitleText(MonthViewAdapter adapter) {
        Context context = getContext();
        int month = adapter.getMonth();
        int year = adapter.getYear();
        String monthString = DateUtils.monthNominativeName(context, month);
        String text = context.getString(R.string.calendar_month_title, monthString, year);
        return text;
    }

    @Override
    protected boolean showEventsHeaderNavigationButtons() {
        return false;
    }
}