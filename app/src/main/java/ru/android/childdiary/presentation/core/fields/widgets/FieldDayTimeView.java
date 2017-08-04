package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import org.joda.time.LocalTime;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.utils.strings.DateUtils;

public class FieldDayTimeView extends FieldDialogView<LocalTime> {
    @BindView(R.id.label)
    TextView label;

    private String text;

    public FieldDayTimeView(Context context) {
        super(context);
    }

    public FieldDayTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldDayTimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init(@Nullable AttributeSet attrs) {
        super.init(attrs);
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.FieldDayTimeView, 0, 0);
            try {
                text = ta.getString(R.styleable.FieldDayTimeView_fieldText);
            } finally {
                ta.recycle();
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        label.setText(text);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_day_time;
    }

    @Nullable
    @Override
    protected String getTextForValue(@Nullable LocalTime value) {
        return DateUtils.time(getContext(), value);
    }
}
