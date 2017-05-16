package ru.android.childdiary.presentation.core.adapters.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.core.adapters.swipe.ListDiff;

public abstract class BaseRecyclerViewAdapter<T, VH extends BaseRecyclerViewHolder<T>>
        extends RecyclerView.Adapter<VH> implements ListDiff.Callback<T> {
    protected final Context context;
    protected final LayoutInflater inflater;

    protected Sex sex;
    protected List<T> items;

    public BaseRecyclerViewAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.items = new ArrayList<>();
    }

    public final void setSex(@Nullable Sex sex) {
        if (this.sex != sex) {
            this.sex = sex;
            notifyDataSetChanged();
        }
    }

    public final void setItems(@NonNull List<T> items) {
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
    public void onBindViewHolder(VH viewHolder, int position) {
        viewHolder.bind(context, sex, items.get(position));
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

    protected abstract VH createViewHolder(ViewGroup parent);

    @Override
    public boolean areContentsTheSame(T oldItem, T newItem) {
        return oldItem.equals(newItem);
    }
}