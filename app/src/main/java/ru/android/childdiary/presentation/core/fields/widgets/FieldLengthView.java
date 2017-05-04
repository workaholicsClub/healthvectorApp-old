package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ListAdapter;

import java.util.Collections;
import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.fields.adapters.LengthAdapter;
import ru.android.childdiary.utils.TimeUtils;

public class FieldLengthView extends FieldSpinnerView<Integer> {
    private final LengthAdapter adapter = new LengthAdapter(getContext(), Collections.emptyList());

    public FieldLengthView(Context context) {
        super(context);
    }

    public FieldLengthView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldLengthView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_length;
    }

    @Override
    protected String getTextForValue(@Nullable Integer value) {
        return TimeUtils.durationLong(getContext(), value);
    }

    @Override
    protected ListAdapter getAdapter() {
        return adapter;
    }

    public void updateAdapter(List<Integer> items) {
        adapter.setItems(items);
    }
}
