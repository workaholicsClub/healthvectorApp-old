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
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.presentation.core.fields.adapters.MedicineAdapter;

public class FieldMedicineView extends FieldSpinnerView<Medicine> {
    private final MedicineAdapter adapter = new MedicineAdapter(getContext(), Collections.emptyList());

    public FieldMedicineView(Context context) {
        super(context);
    }

    public FieldMedicineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldMedicineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_medicine;
    }

    @Override
    protected String getTextForValue(@Nullable Medicine value) {
        return value == null ? null : value.getName();
    }

    @Override
    protected ListAdapter getAdapter() {
        return adapter;
    }

    public void updateAdapter(List<Medicine> items) {
        items = new ArrayList<>(items);
        // TODO item Others?
        adapter.setItems(items);
    }
}
