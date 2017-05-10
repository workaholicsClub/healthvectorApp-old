package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.LocalTime;

import java.util.ArrayList;

import butterknife.ButterKnife;
import lombok.Getter;
import ru.android.childdiary.domain.interactors.core.LinearGroups;
import ru.android.childdiary.utils.DateUtils;

public class FieldTimesView extends LinearLayout implements FieldReadOnly {
    @Nullable
    @Getter
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
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
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

    public void setNumber(@Nullable Integer number) {
        if (number == null || number < 0) {
            linearGroups = null;
            return;
        }
        ArrayList<LocalTime> times = new ArrayList<>();
        for (int i = 0; i < number; ++i) {
            times.add(LocalTime.MIDNIGHT.plusHours(i));
        }
        linearGroups = LinearGroups.builder().times(times).build();
        update();
    }

    public void setLinearGroups(@Nullable LinearGroups linearGroups) {
        this.linearGroups = linearGroups;
        update();
    }

    private void update() {
        removeAllViews();
        if (linearGroups == null) {
            return;
        }
        for (int i = 0; i < linearGroups.getTimes().size(); ++i) {
            TextView textView = new TextView(getContext());
            textView.setText(DateUtils.time(getContext(), linearGroups.getTimes().get(i)));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 10, 10, 10);
            textView.setLayoutParams(layoutParams);
            addView(textView);
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        // TODO not clickable, font, etc.
    }
}
