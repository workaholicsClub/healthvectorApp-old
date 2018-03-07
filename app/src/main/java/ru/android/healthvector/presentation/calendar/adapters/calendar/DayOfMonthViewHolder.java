package ru.android.healthvector.presentation.calendar.adapters.calendar;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.joda.time.LocalDate;

import butterknife.BindView;
import ru.android.healthvector.R;
import ru.android.healthvector.presentation.core.adapters.BaseViewHolder;
import ru.android.healthvector.utils.ui.FontUtils;
import ru.android.healthvector.utils.ui.ResourcesUtils;

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
        Context context = adapter.getContext();
        boolean isCurrent = LocalDate.now().isEqual(item);
        boolean isSelected = adapter.isSelected() && adapter.getSelectedDate().isEqual(item);
        textViewDayOfMonth.setBackgroundResource(
                isSelected
                        ? ResourcesUtils.getSelectedDateBackgroundRes(adapter.getSex())
                        : (isCurrent ? R.drawable.calendar_cell_background_current_day : R.drawable.calendar_cell_background_clickable));
        //noinspection deprecation
        textViewDayOfMonth.setTextAppearance(context,
                isSelected
                        ? R.style.CalendarDayOfMonthSelectedTextAppearance
                        : (isCurrent ? R.style.CalendarDayOfMonthCurrentTextAppearance : R.style.CalendarDayOfMonthTextAppearance));
        textViewDayOfMonth.setTypeface(
                isCurrent || isSelected
                        ? FontUtils.getTypefaceBold(context)
                        : FontUtils.getTypefaceRegular(context));
    }
}
