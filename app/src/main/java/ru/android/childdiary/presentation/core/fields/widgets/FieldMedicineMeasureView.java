package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ListAdapter;

import java.util.Collections;
import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;
import ru.android.childdiary.presentation.core.fields.adapters.MedicineMeasureAdapter;

public class FieldMedicineMeasureView extends FieldSpinnerView<MedicineMeasure> {
    private final MedicineMeasureAdapter adapter = new MedicineMeasureAdapter(getContext(), Collections.emptyList());

    public FieldMedicineMeasureView(Context context) {
        super(context);
    }

    public FieldMedicineMeasureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldMedicineMeasureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_medicine_measure;
    }

    @Override
    protected String getTextForValue(@Nullable MedicineMeasure value) {
        return value == null ? null : value.getName();
    }

    @Override
    protected ListAdapter getAdapter() {
        return adapter;
    }

    public void updateAdapter(List<MedicineMeasure> items) {
        adapter.setItems(items);
    }
}
