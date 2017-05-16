package ru.android.childdiary.presentation.core.adapters.swipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.Getter;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;

public abstract class SwipeViewAdapter<
        T,
        VH extends SwipeViewHolder<T, SL, IL>,
        SL extends SwipeActionListener<VH>,
        IL extends ItemActionListener<T>>
        extends BaseRecyclerViewAdapter<T, VH>
        implements SwipeActionListener<VH> {
    protected final IL itemActionListener;
    @Getter
    protected final SwipeManager swipeManager;

    public SwipeViewAdapter(Context context, @NonNull IL itemActionListener, @Nullable FabController fabController) {
        super(context);
        this.itemActionListener = itemActionListener;
        this.swipeManager = new SwipeManager(fabController);
    }

    @Override
    public final void onBindViewHolder(VH viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        swipeManager.bindViewHolder(viewHolder, position);
    }

    @Override
    public final void delete(VH viewHolder) {
        viewHolder.getSwipeLayout().addSwipeListener(new SwipeDeleteAction<>(viewHolder.getItem(), itemActionListener));
        swipeManager.closeAllItems();
    }
}