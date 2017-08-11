package ru.android.childdiary.presentation.core.adapters.recycler;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.core.adapters.swipe.ListDiff;

public abstract class BaseRecyclerViewAdapter<T, VH extends BaseRecyclerViewHolder<T>>
        extends RecyclerView.Adapter<VH> implements ListDiff.Callback<T> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    protected final Context context;
    protected final LayoutInflater inflater;
    @Nullable
    protected Sex sex;
    protected List<T> items = new ArrayList<>();
    @Nullable
    private RecyclerView recyclerView;

    public BaseRecyclerViewAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public final void setSex(@Nullable Sex sex) {
        if (this.sex != sex) {
            this.sex = sex;
            notifyDataSetChanged();
        }
    }

    public final List<T> getItems() {
        return new ArrayList<>(items);
    }

    @CallSuper
    public void setItems(@NonNull List<T> items) {
        if (this.items.isEmpty()) {
            this.items.clear();
            this.items.addAll(items);
            notifyDataSetChanged();
            return;
        }

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ListDiff<>(this.items, items, this), false);
        this.items.clear();
        this.items.addAll(items);
        diffResult.dispatchUpdatesTo(this);

        if (recyclerView != null) {
            new Handler().post(() -> recyclerView.invalidateItemDecorations());
        }
    }

    public final boolean deleteItem(T item) {
        int position = items.indexOf(item);
        if (position < 0) {
            return false;
        }
        items.remove(item);
        notifyItemRemoved(position);
        return true;
    }

    public final void updatePartially(@NonNull T item) {
        for (int i = 0; i < items.size(); ++i) {
            if (areItemsTheSame(items.get(i), item)) {
                items.set(i, item);
                notifyItemChanged(i, new Object());
                break;
            }
        }
    }

    public final void updateItem(@NonNull T item) {
        for (int i = 0; i < items.size(); ++i) {
            if (areItemsTheSame(items.get(i), item)) {
                items.set(i, item);
                notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public final int getItemViewType(int position) {
        return position == items.size() - 1 ? TYPE_FOOTER : TYPE_ITEM;
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                return createViewHolder(parent);
            case TYPE_FOOTER:
                return createViewHolder(parent);
        }
        return null;
    }

    protected abstract VH createViewHolder(ViewGroup parent);

    private FooterViewHolder<T> createFooterViewHolder(ViewGroup parent) {
        View v = inflater.inflate(R.layout.picker_item, parent, false);
        return new FooterViewHolder<>(v);
    }

    @CallSuper
    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        if (position < items.size()) {
            viewHolder.bind(context, sex, items.get(position));
        }
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
        return items.size() + 1;
    }

    @Override
    public final boolean areContentsTheSame(T oldItem, T newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    public final void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public final void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }
}
