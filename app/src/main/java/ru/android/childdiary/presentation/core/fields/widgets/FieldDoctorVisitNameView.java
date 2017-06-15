package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.util.AttributeSet;

import butterknife.BindInt;
import ru.android.childdiary.R;

public class FieldDoctorVisitNameView extends FieldExitTextWithImageView {
    @BindInt(R.integer.max_length_doctor_visit_name)
    int MAX_LENGTH;

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
        return R.drawable.ic_doctor_visit_name;
    }

    @Override
    protected int getHintResId() {
        return R.string.doctor_visit_title_hint;
    }

    @Override
    protected int getMaxLength() {
        return MAX_LENGTH;
    }
}
