package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.util.AttributeSet;

import butterknife.BindInt;
import ru.android.childdiary.R;

public class FieldOtherEventNameView extends FieldExitTextWithImageView {
    @BindInt(R.integer.max_length_other_event_name)
    int MAX_LENGTH;

    public FieldOtherEventNameView(Context context) {
        super(context);
    }

    public FieldOtherEventNameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldOtherEventNameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected int getIconResId() {
        return R.drawable.ic_other_event_name;
    }

    @Override
    protected int getHintResId() {
        return R.string.other_event_title_hint;
    }

    @Override
    protected int getMaxLength() {
        return MAX_LENGTH;
    }
}
