package ru.android.childdiary.presentation.main.calendar.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.joda.time.LocalDate;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.adapters.BaseViewHolder;
import ru.android.childdiary.utils.ui.ThemeUtils;

class DayOfMonthViewHolder extends BaseViewHolder<LocalDate> {
    @BindView(R.id.textViewDayOfMonth)
    TextView textViewDayOfMonth;

    public DayOfMonthViewHolder(View view) {
        super(view);
    }

    @Override
    public void bind(Context context, int position, LocalDate item) {
        textViewDayOfMonth.setText(String.valueOf(item.getDayOfMonth()));
    }

    public void select(CalendarViewAdapter adapter, LocalDate item) {
        boolean isSelected = adapter.getSelectedDate().isEqual(item);
        textViewDayOfMonth.setBackgroundColor(isSelected
                ? ThemeUtils.getColorAccent(adapter.getContext(), adapter.getSex())
                : 0);
    }
}
