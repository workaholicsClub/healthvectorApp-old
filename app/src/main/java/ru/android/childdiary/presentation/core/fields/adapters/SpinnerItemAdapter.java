package ru.android.childdiary.presentation.core.fields.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.adapters.BaseArrayAdapter;

abstract class SpinnerItemAdapter<T, VH extends SpinnerItemViewHolder<T>> extends BaseArrayAdapter<T, VH> {
    public SpinnerItemAdapter(Context context) {
        super(context);
    }

    public SpinnerItemAdapter(Context context, @NonNull List<T> items) {
        super(context, items);
    }

    public SpinnerItemAdapter(Context context, @NonNull T[] items) {
        super(context, Arrays.asList(items));
    }

    @Override
    @LayoutRes
    protected final int getLayoutResourceId() {
        return R.layout.spinner_item;
    }
}
