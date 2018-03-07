package ru.android.healthvector.presentation.core.adapters.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import ru.android.healthvector.data.types.Sex;

class FooterViewHolder<T> extends BaseRecyclerViewHolder<T> {
    public FooterViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void bind(Context context, @Nullable Sex sex) {
    }
}
