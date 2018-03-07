package ru.android.healthvector.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ListAdapter;

import java.util.Collections;
import java.util.List;

import ru.android.healthvector.R;
import ru.android.healthvector.domain.calendar.data.core.PeriodicityType;
import ru.android.healthvector.presentation.core.fields.adapters.PeriodicityAdapter;
import ru.android.healthvector.utils.strings.StringUtils;

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

    @Nullable
    @Override
    protected String getTextForValue(@Nullable PeriodicityType value) {
        return StringUtils.periodicity(getContext(), value);
    }

    @Override
    protected ListAdapter getAdapter() {
        return adapter;
    }

    public void updateAdapter(List<PeriodicityType> items) {
        adapter.setItems(items);
    }
}
