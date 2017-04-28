package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.presentation.core.fields.adapters.DoctorAdapter;

public class FieldDoctorView extends FieldSpinnerView<Doctor> {
    private final DoctorAdapter adapter = new DoctorAdapter(getContext(), Collections.emptyList());

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

    @Override
    protected ListAdapter getAdapter() {
        return adapter;
    }

    public void updateAdapter(List<Doctor> items) {
        items = new ArrayList<>(items);
        // TODO item Others?
        adapter.setItems(items);
    }
}
