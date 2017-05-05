package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.LocalTime;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.childdiary.R;
import ru.android.childdiary.data.entities.calendar.events.core.LinearGroups;
import ru.android.childdiary.utils.DateUtils;
import ru.android.childdiary.utils.ObjectUtils;

public class FieldTimesView extends LinearLayout {
    @BindView(R.id.timesView)
    LinearLayout timesView;

    @Nullable
    private LinearGroups linearGroups;

    public FieldTimesView(Context context) {
        super(context);
        init();
    }

    public FieldTimesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FieldTimesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.field_times, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void setStartTime(LocalTime startTime) {
    }

    public void setFinishTime(LocalTime finishTime) {
    }

    public void setNumber(@NonNull Integer number) {
        if (ObjectUtils.isPositive(number)) {
            ArrayList<LocalTime> times = new ArrayList<>();
            for (int i = 0; i < number; ++i) {
                times.add(LocalTime.MIDNIGHT);
            }
            linearGroups = LinearGroups.builder().times(times).build();
        } else {
            linearGroups = null;
        }
        update();
    }

    public void setLinearGroups(@Nullable LinearGroups linearGroups) {
        this.linearGroups = linearGroups;
        update();
    }

    @Nullable
    public LinearGroups getLinearGroups() {
        return linearGroups;
    }

    private void update() {
        timesView.removeAllViews();
        if (linearGroups == null) {
            return;
        }
        for (int i = 0; i < linearGroups.getTimes().size(); ++i) {
            TextView textView = new TextView(getContext());
            textView.setText(DateUtils.time(getContext(), linearGroups.getTimes().get(i)));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 10, 10, 10);
            textView.setLayoutParams(layoutParams);
            timesView.addView(textView);
        }
    }
}
