package ru.android.childdiary.presentation.core.adapters.swipe;

import android.support.annotation.NonNull;
import android.view.View;

import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;

public abstract class SwipeViewHolder<T,
        SL extends SwipeActionListener<? extends SwipeViewHolder<T, SL, IL>>,
        IL extends ItemActionListener<T>>
        extends BaseRecyclerViewHolder<T> implements SwipeLayoutContainer {
    protected final IL itemActionListener;
    protected final SL swipeActionListener;

    public SwipeViewHolder(View itemView, @NonNull IL itemActionListener, @NonNull SL swipeActionListener) {
        super(itemView);
        this.itemActionListener = itemActionListener;
        this.swipeActionListener = swipeActionListener;
    }
}
