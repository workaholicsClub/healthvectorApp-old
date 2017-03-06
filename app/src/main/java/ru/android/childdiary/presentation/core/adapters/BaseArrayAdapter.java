package ru.android.childdiary.presentation.core.adapters;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.Collections;
import java.util.List;

public abstract class BaseArrayAdapter<T, VH extends BaseViewHolder<T>> extends ArrayAdapter<T> {
    private final LayoutInflater inflater;
    private final List<T> items;

    public BaseArrayAdapter(Context context, @NonNull List<T> items) {
        super(context, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = Collections.unmodifiableList(items);
    }

    private View inflate(@LayoutRes int resourceId) {
        return inflater.inflate(resourceId, null);
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
            view = inflate(getLayoutResourceId());
            viewHolder = createViewHolder(view);
            view.setTag(viewHolder);
        } else {
            //noinspection unchecked
            viewHolder = (VH) view.getTag();
        }

        T item = getItem(position);
        bind(position, item, viewHolder);

        return view;
    }

    @CallSuper
    protected void bind(int position, T item, VH viewHolder) {
        viewHolder.bind(getContext(), position, item);
    }

    @LayoutRes
    protected abstract int getLayoutResourceId();

    protected abstract VH createViewHolder(View view);
}
