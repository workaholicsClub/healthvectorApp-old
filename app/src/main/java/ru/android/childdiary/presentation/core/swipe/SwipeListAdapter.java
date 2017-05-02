package ru.android.childdiary.presentation.core.swipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import ru.android.childdiary.data.types.Sex;

public abstract class SwipeListAdapter<
        VH extends SwipeViewHolder<T, SL, IL>,
        T,
        SL extends SwipeActionListener<VH>,
        IL extends ItemActionListener<T>>
        extends RecyclerView.Adapter<VH>
        implements SwipeActionListener<VH>, ListDiff.Callback<T> {
    protected final Context context;
    protected final LayoutInflater inflater;
    protected final IL itemActionListener;
    @Getter
    protected final SwipeManager swipeManager;

    protected Sex sex;
    protected List<T> items;

    public SwipeListAdapter(Context context, @NonNull IL itemActionListener, @Nullable FabController fabController) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.itemActionListener = itemActionListener;
        this.swipeManager = new SwipeManager(fabController);
        this.items = new ArrayList<>();
    }

    public final void setSex(@Nullable Sex sex) {
        if (this.sex != sex) {
            this.sex = sex;
            notifyDataSetChanged();
        }
    }

    public final void setItems(@NonNull List<T> items) {
        swipeManager.closeAllItems();
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ListDiff<>(this.items, items, this), false);
        diffResult.dispatchUpdatesTo(this);
        this.items = new ArrayList<>(items);
    }

    public final void updatePartially(@NonNull T item) {
        for (int i = 0; i < items.size(); ++i) {
            if (areItemsTheSame(items.get(i), item)) {
                items.set(i, item);
                notifyItemChanged(i, new Object());
            }
        }
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH viewHolder = createViewHolder(parent);
        return viewHolder;
    }

    @Override
    public final void onBindViewHolder(VH viewHolder, int position) {
        viewHolder.bind(context, sex, items.get(position));
        swipeManager.bindViewHolder(viewHolder, position);
    }

    @Override
    public final void onBindViewHolder(VH viewHolder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(viewHolder, position);
        } else {
            viewHolder.updatePartially(context, items.get(position));
        }
    }

    @Override
    public final int getItemCount() {
        return items.size();
    }

    @Override
    public final void delete(VH viewHolder) {
        viewHolder.getSwipeLayout().addSwipeListener(new SwipeDeleteAction<>(viewHolder.getItem(), itemActionListener));
        swipeManager.closeAllItems();
    }

    protected abstract VH createViewHolder(ViewGroup parent);

    @Override
    public boolean areContentsTheSame(T oldItem, T newItem) {
        return oldItem.equals(newItem);
    }
}
