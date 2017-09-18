package ru.android.childdiary.presentation.core.adapters.swipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;

public abstract class SwipeViewAdapter<
        T,
        VH extends SwipeViewHolder<T, SL, IL>,
        SL extends SwipeActionListener<VH>,
        IL extends ItemActionListener<T>>
        extends BaseRecyclerViewAdapter<T, VH>
        implements SwipeActionListener<VH>, SwipeController {
    protected final IL itemActionListener;
    private final SwipeManager swipeManager;

    public SwipeViewAdapter(Context context, @NonNull IL itemActionListener, @Nullable FabController fabController) {
        super(context);
        this.itemActionListener = itemActionListener;
        this.swipeManager = new SwipeManager(fabController);
    }

    @Override
    public final void setItems(@NonNull List<T> items) {
        super.setItems(items);
        swipeManager.closeAllItems();
    }

    @Override
    protected final void bindUserViewHolder(VH viewHolder, int position) {
        super.bindUserViewHolder(viewHolder, position);
        swipeManager.bindViewHolder(viewHolder, position);
    }

    @Override
    public final void delete(VH viewHolder) {
        viewHolder.getSwipeLayout().addSwipeListener(new SwipeDeleteAction<>(viewHolder.getItem(), itemActionListener));
        swipeManager.closeAllItems();
    }

    @Override
    public void closeAllItems() {
        swipeManager.closeAllItems();
    }

    @Override
    public void setFabController(@Nullable FabController fabController) {
        swipeManager.setFabController(fabController);
    }

    @Override
    public void updateFabState() {
        swipeManager.updateFabState();
    }
}
