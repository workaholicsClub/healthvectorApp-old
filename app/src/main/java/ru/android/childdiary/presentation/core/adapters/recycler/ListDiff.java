package ru.android.childdiary.presentation.core.adapters.recycler;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.Collections;
import java.util.List;

class ListDiff<T> extends DiffUtil.Callback {
    private final List<T> oldItems;
    private final List<T> newItems;
    @Nullable
    private final Callback<T> callback;

    public ListDiff(List<T> oldItems, List<T> newItems) {
        this(oldItems, newItems, null);
    }

    public ListDiff(List<T> oldItems, List<T> newItems, @Nullable Callback<T> callback) {
        this.oldItems = Collections.unmodifiableList(oldItems);
        this.newItems = Collections.unmodifiableList(newItems);
        this.callback = callback;
    }

    @Override
    public int getOldListSize() {
        return oldItems.size();
    }

    @Override
    public int getNewListSize() {
        return newItems.size();
    }

    @Override
    public final boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T oldItem = oldItems.get(oldItemPosition);
        T newItem = newItems.get(newItemPosition);
        return areItemsTheSame(oldItem, newItem);
    }

    @Override
    public final boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T oldItem = oldItems.get(oldItemPosition);
        T newItem = newItems.get(newItemPosition);
        return areContentsTheSame(oldItem, newItem);
    }

    protected boolean areItemsTheSame(T oldItem, T newItem) {
        return callback != null && callback.areItemsTheSame(oldItem, newItem);
    }

    protected boolean areContentsTheSame(T oldItem, T newItem) {
        return callback != null && callback.areContentsTheSame(oldItem, newItem);
    }

    public interface Callback<T> {
        boolean areItemsTheSame(T oldItem, T newItem);

        boolean areContentsTheSame(T oldItem, T newItem);
    }
}
