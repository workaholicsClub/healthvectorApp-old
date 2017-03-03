package ru.android.childdiary.presentation.main.calendar.adapters;

import android.content.Context;
import android.widget.TextView;

import org.joda.time.LocalDate;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.adapters.BaseViewHolder;

class DayOfMonthViewHolder extends BaseViewHolder<LocalDate> {
    @BindView(R.id.textViewDayOfMonth)
    TextView textViewDayOfMonth;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.grid_cell_day_of_month;
    }

    @Override
    public void bind(Context context, int position, LocalDate item) {
        textViewDayOfMonth.setText(String.valueOf(item.getDayOfMonth()));
    }
}
