package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;

import ru.android.childdiary.R;

public class FieldDoctorVisitNameView extends FieldNameView {
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
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_doctor_visit_name;
    }
}
