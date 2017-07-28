package ru.android.childdiary.presentation.core.adapters.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
import lombok.Getter;
import ru.android.childdiary.data.types.Sex;

public abstract class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder {
    @Getter
    protected T item;

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Context context, @Nullable Sex sex, T item) {
        this.item = item;
    }

    public void updatePartially(Context context, T item) {
    }
}
