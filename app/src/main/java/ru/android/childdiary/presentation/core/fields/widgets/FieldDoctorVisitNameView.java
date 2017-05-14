package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import ru.android.childdiary.R;

public class FieldDoctorVisitNameView extends FieldNameView implements FieldReadOnly {
    public FieldDoctorVisitNameView(Context context) {
        super(context);
    }

    public FieldDoctorVisitNameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldDoctorVisitNameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected int getIconResId() {
        return R.drawable.ic_other_event_name;
    }

    @Override
    protected int getHintResId() {
        return R.string.doctor_visit_title_hint;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        setVisibility(TextUtils.isEmpty(getText()) ? GONE : VISIBLE);
        editText.setEnabled(!readOnly);
    }
}
