package ru.android.healthvector.presentation.medical.filter.visits;

import android.content.Context;
import android.util.AttributeSet;

import ru.android.healthvector.domain.dictionaries.doctors.data.Doctor;
import ru.android.healthvector.presentation.medical.filter.core.BaseTokenCompleteTextView;

public class DoctorTokenCompleteTextView extends BaseTokenCompleteTextView<Doctor> {
    public DoctorTokenCompleteTextView(Context context) {
        super(context);
    }

    public DoctorTokenCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DoctorTokenCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected Doctor defaultObject(String completionText) {
        return Doctor.NULL;
    }

    @Override
    protected String getTextForValue(Doctor item) {
        return item.getName();
    }
}
