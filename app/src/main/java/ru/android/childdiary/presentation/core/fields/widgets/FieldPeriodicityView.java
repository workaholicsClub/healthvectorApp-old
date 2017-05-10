package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ListAdapter;

import java.util.Collections;
import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.core.PeriodicityType;
import ru.android.childdiary.presentation.core.fields.adapters.PeriodicityAdapter;
import ru.android.childdiary.utils.TimeUtils;

public class FieldPeriodicityView extends FieldSpinnerView<PeriodicityType> {
    private final PeriodicityAdapter adapter = new PeriodicityAdapter(getContext(), Collections.emptyList());

    public FieldPeriodicityView(Context context) {
        super(context);
    }

    public FieldPeriodicityView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldPeriodicityView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_periodicity;
    }

    @Override
    protected String getTextForValue(@Nullable PeriodicityType value) {
        return TimeUtils.periodicity(getContext(), value);
    }

    @Override
    protected ListAdapter getAdapter() {
        return adapter;
    }

    public void updateAdapter(List<PeriodicityType> items) {
        adapter.setItems(items);
    }
}
