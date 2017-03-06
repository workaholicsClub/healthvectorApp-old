package ru.android.childdiary.presentation.core.adapters;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import lombok.Getter;

public abstract class BaseTwoTypesAdapter<VH0 extends BaseViewHolder<T0>, T0, VH1 extends BaseViewHolder<T1>, T1> extends BaseAdapter {
    @Getter
    private final Context context;
    private final LayoutInflater inflater;

    public BaseTwoTypesAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private View inflate(@LayoutRes int resourceId) {
        return inflater.inflate(resourceId, null);
    }

    @Override
    public final int getViewTypeCount() {
        return 2;
    }

    @Override
    public final int getItemViewType(int position) {
        return isItemOfFirstType(position) ? 0 : 1;
    }

    @Override
    public final Object getItem(int position) {
        if (isItemOfFirstType(position)) {
            return getFirstTypeItem(position);
        } else {
            return getSecondTypeItem(position);
        }
    }

    @Override
    public final long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (isItemOfFirstType(position)) {
            VH0 viewHolder;
            if (view == null) {
                view = inflate(getFirstTypeLayoutResourceId());
                viewHolder = createFirstTypeViewHolder(view);
                view.setTag(viewHolder);
            } else {
                //noinspection unchecked
                viewHolder = (VH0) view.getTag();
            }

            T0 item = getFirstTypeItem(position);
            bindFirst(position, item, viewHolder);
        } else {
            VH1 viewHolder;
            if (view == null) {
                view = inflate(getSecondTypeLayoutResourceId());
                viewHolder = createSecondTypeViewHolder(view);
                view.setTag(viewHolder);
            } else {
                //noinspection unchecked
                viewHolder = (VH1) view.getTag();
            }

            T1 item = getSecondTypeItem(position);
            bindSecond(position, item, viewHolder);
        }

        return view;
    }

    @CallSuper
    protected void bindFirst(int position, T0 item, VH0 viewHolder) {
        viewHolder.bind(getContext(), position, item);
    }

    @CallSuper
    protected void bindSecond(int position, T1 item, VH1 viewHolder) {
        viewHolder.bind(getContext(), position, item);
    }


    protected abstract boolean isItemOfFirstType(int position);

    protected abstract T0 getFirstTypeItem(int position);

    protected abstract T1 getSecondTypeItem(int position);

    @LayoutRes
    protected abstract int getFirstTypeLayoutResourceId();

    @LayoutRes
    protected abstract int getSecondTypeLayoutResourceId();

    protected abstract VH0 createFirstTypeViewHolder(View view);

    protected abstract VH1 createSecondTypeViewHolder(View view);
}
