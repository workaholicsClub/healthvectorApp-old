package ru.android.childdiary.presentation.settings.adapters;

import android.content.Context;
import android.view.View;

import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.childdiary.presentation.settings.adapters.items.BaseSettingsItem;

public abstract class BaseSettingsViewHolder<T extends BaseSettingsItem> extends BaseRecyclerViewHolder<BaseSettingsItem> {
    public BaseSettingsViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public final void bind(Context context, Sex sex, BaseSettingsItem item) {
        super.bind(context, sex, item);
        //noinspection unchecked
        bindT(context, sex, (T) item);
    }

    protected abstract void bindT(Context context, Sex sex, T item);

    protected T getItemT() {
        //noinspection unchecked
        return (T) item;
    }
}
