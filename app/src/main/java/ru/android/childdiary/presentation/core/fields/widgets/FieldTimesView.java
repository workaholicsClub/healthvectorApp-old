package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindDimen;
import butterknife.ButterKnife;
import lombok.Getter;
import lombok.Setter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.core.LinearGroups;
import ru.android.childdiary.utils.DateUtils;
import ru.android.childdiary.utils.ui.FontUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class FieldTimesView extends LinearLayout implements FieldReadOnly, View.OnClickListener {
    private final Typeface typeface = FontUtils.getTypefaceRegular(getContext());

    @BindDimen(R.dimen.times_margin)
    int margin;

    @BindDimen(R.dimen.times_padding)
    int padding;

    @Nullable
    private Sex sex;
    private boolean readOnly;

    @Nullable
    @Getter
    private LinearGroups linearGroups;

    @Nullable
    @Setter
    private FieldTimesListener fieldTimesListener;

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
        update();
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

    public void setSex(@Nullable Sex sex) {
        if (this.sex != sex) {
            this.sex = sex;
            update();
        }
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
            layoutParams.setMargins(margin, 0, margin, 0);
            textView.setLayoutParams(layoutParams);
            textView.setPadding(padding, padding, padding, padding);
            //noinspection deprecation
            textView.setTextAppearance(getContext(), readOnly ? R.style.SecondaryTextAppearance : R.style.PrimaryTextAppearance);
            textView.setTypeface(typeface);
            textView.setBackgroundResource(ResourcesUtils.getTimeItemBackgroundRes(sex, !readOnly));
            textView.setOnClickListener(readOnly ? null : this);
            textView.setTag(i);
            addView(textView);
        }
    }

    @Override
    public void onClick(View v) {
        if (linearGroups == null) {
            return;
        }
        Integer i = (Integer) v.getTag();
        LocalTime time = linearGroups.getTimes().get(i);
        if (fieldTimesListener != null) {
            fieldTimesListener.requestValueChange(i, time);
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        update();
    }

    public void setTime(int i, LocalTime time) {
        if (linearGroups == null) {
            return;
        }
        linearGroups = linearGroups.toBuilder().build();
        linearGroups.getTimes().set(i, time);
        Collections.sort(linearGroups.getTimes());
        update();
    }

    public interface FieldTimesListener {
        void requestValueChange(int i, LocalTime time);
    }
}
