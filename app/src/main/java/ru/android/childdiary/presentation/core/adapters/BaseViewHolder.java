package ru.android.childdiary.presentation.core.adapters;

import android.content.Context;
import android.view.View;

import butterknife.ButterKnife;

public abstract class BaseViewHolder<T> {
    public BaseViewHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public abstract void bind(Context context, int position, T item);
}
