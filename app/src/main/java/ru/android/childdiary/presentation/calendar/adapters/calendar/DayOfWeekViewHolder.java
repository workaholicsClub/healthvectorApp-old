package ru.android.childdiary.presentation.calendar.adapters.calendar;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.adapters.BaseViewHolder;
import ru.android.childdiary.utils.ui.FontUtils;

class DayOfWeekViewHolder extends BaseViewHolder<String> {
    @BindView(R.id.textViewDayOfWeek)
    TextView textViewDayOfWeek;

    public DayOfWeekViewHolder(View view) {
        super(view);
    }

    @Override
    public void bind(Context context, int position, String item) {
        textViewDayOfWeek.setText(item);
        textViewDayOfWeek.setTypeface(FontUtils.getTypefaceBold(context));
    }
}
