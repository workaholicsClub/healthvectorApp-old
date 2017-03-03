package ru.android.childdiary.presentation.core.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.Collections;
import java.util.List;

public abstract class BaseArrayAdapter<T, VH extends BaseViewHolder<T>> extends ArrayAdapter<T> {
    private final List<T> items;

    public BaseArrayAdapter(Context context, @NonNull List<T> items) {
        super(context, 0);
        this.items = Collections.unmodifiableList(items);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Nullable
    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        VH viewHolder;
        if (view == null) {
            viewHolder = createViewHolder();
            view = viewHolder.inflate(getContext());
            view.setTag(viewHolder);
        } else {
            viewHolder = (VH) view.getTag();
        }

        T item = getItem(position);
        viewHolder.bind(getContext(), position, item);

        return view;
    }

    protected abstract VH createViewHolder();
}
