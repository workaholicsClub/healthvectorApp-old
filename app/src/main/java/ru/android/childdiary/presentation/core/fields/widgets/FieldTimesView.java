package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.LocalTime;

import java.util.List;

import butterknife.BindDimen;
import lombok.Setter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.calendar.data.core.LinearGroups;
import ru.android.childdiary.utils.TimesMatcher;
import ru.android.childdiary.utils.strings.DateUtils;
import ru.android.childdiary.utils.ui.FontUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class FieldTimesView extends BaseFieldValueView<LinearGroups> implements View.OnClickListener {
    private final Typeface typeface = FontUtils.getTypefaceRegular(getContext());

    @BindDimen(R.dimen.times_margin)
    int margin;

    @BindDimen(R.dimen.times_padding)
    int padding;

    @Nullable
    private Sex sex;
    private boolean readOnly;

    @Setter
    private LocalTime startTime = LocalTime.MIDNIGHT;
    @Setter
    private LocalTime finishTime = LocalTime.MIDNIGHT;

    @Nullable
    @Setter
    private FieldTimesListener fieldTimesListener;

    public FieldTimesView(Context context) {
        super(context);
    }

    public FieldTimesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldTimesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init(@Nullable AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return 0;
    }

    @Override
    protected void valueChanged() {
        update();
    }

    public void setNumber(@Nullable Integer number) {
        LinearGroups linearGroups;
        if (number == null || number < 0) {
            linearGroups = null;
        } else {
            List<LocalTime> times = TimesMatcher.match(startTime, finishTime, number);
            linearGroups = LinearGroups.builder().times(times).build();
        }
        setValue(linearGroups);
    }

    public void setSex(@Nullable Sex sex) {
        if (this.sex != sex) {
            this.sex = sex;
            update();
        }
    }

    private void update() {
        removeAllViews();
        if (getValue() == null) {
            return;
        }
        for (int i = 0; i < getValue().getTimes().size(); ++i) {
            TextView textView = new TextView(getContext());
            textView.setText(DateUtils.time(getContext(), getValue().getTimes().get(i)));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(margin, 0, margin, 0);
            textView.setLayoutParams(layoutParams);
            textView.setPadding(padding, padding, padding, padding);
            textView.setTypeface(typeface);
            textView.setBackgroundResource(ResourcesUtils.getTimeItemBackgroundRes(sex, !readOnly));
            textView.setOnClickListener(readOnly ? null : this);
            textView.setTag(i);
            addView(textView);
        }
    }

    @Override
    public void onClick(View v) {
        if (getValue() == null) {
            return;
        }
        Integer i = (Integer) v.getTag();
        LocalTime time = getValue().getTimes().get(i);
        if (fieldTimesListener != null) {
            fieldTimesListener.requestValueChange(i, time);
        }
    }

    public void setTime(int i, LocalTime time) {
        if (getValue() == null) {
            return;
        }
        LinearGroups linearGroups = getValue();
        linearGroups = linearGroups.withTime(i, time);
        setValue(linearGroups);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        update();
    }

    public interface FieldTimesListener {
        void requestValueChange(int i, LocalTime time);
    }
}
