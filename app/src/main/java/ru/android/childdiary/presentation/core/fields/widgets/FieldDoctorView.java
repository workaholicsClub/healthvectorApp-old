package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;

public class FieldDoctorView extends FieldDialogView<Doctor> {
    public FieldDoctorView(Context context) {
        super(context);
    }

    public FieldDoctorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldDoctorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_doctor;
    }

    @Override
    protected String getTextForValue(@Nullable Doctor value) {
        return value == null ? null : value.getName();
    }
}
