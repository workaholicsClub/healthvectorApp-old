package ru.android.healthvector.presentation.calendar.adapters.calendar;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import ru.android.healthvector.R;
import ru.android.healthvector.presentation.core.adapters.BaseViewHolder;

class DayOfWeekViewHolder extends BaseViewHolder<String> {
    @BindView(R.id.textViewDayOfWeek)
    TextView textViewDayOfWeek;

    public DayOfWeekViewHolder(View view) {
        super(view);
    }

    @Override
    public void bind(Context context, int position, String item) {
        textViewDayOfWeek.setText(item);
    }
}
