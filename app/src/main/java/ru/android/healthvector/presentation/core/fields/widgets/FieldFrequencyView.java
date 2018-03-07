package ru.android.healthvector.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ListAdapter;

import java.util.Collections;
import java.util.List;

import ru.android.healthvector.R;
import ru.android.healthvector.presentation.core.fields.adapters.FrequencyAdapter;
import ru.android.healthvector.utils.strings.StringUtils;

public class FieldFrequencyView extends FieldSpinnerView<Integer> {
    private final FrequencyAdapter adapter = new FrequencyAdapter(getContext(), Collections.emptyList());

    public FieldFrequencyView(Context context) {
        super(context);
    }

    public FieldFrequencyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldFrequencyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_frequency;
    }

    @Nullable
    @Override
    protected String getTextForValue(@Nullable Integer value) {
        return StringUtils.frequency(getContext(), value);
    }

    @Override
    protected ListAdapter getAdapter() {
        return adapter;
    }

    public void updateAdapter(List<Integer> items) {
        adapter.setItems(items);
    }
}
