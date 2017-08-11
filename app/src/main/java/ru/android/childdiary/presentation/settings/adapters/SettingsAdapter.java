package ru.android.childdiary.presentation.settings.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.childdiary.presentation.settings.adapters.items.BaseSettingsItem;
import ru.android.childdiary.presentation.settings.adapters.items.SettingsItemType;

public class SettingsAdapter extends BaseRecyclerViewAdapter<BaseSettingsItem, BaseSettingsViewHolder<? extends BaseSettingsItem>> {
    public SettingsAdapter(Context context) {
        super(context);
    }

    @Override
    public boolean areItemsTheSame(BaseSettingsItem oldItem, BaseSettingsItem newItem) {
        return oldItem.getId() == newItem.getId();
    }

    /* // TODO !!!
        @Override
        public int getItemViewType(int position) {
            BaseSettingsItem item = items.get(position);
            return item.getType().ordinal();
        }
    */
    @Override
    protected final BaseSettingsViewHolder<? extends BaseSettingsItem> createViewHolder(ViewGroup parent) {
        // TODO !!!
        int viewType = 0;
        View v;
        SettingsItemType type = SettingsItemType.values()[viewType];
        switch (type) {
            case DELIMITER:
                v = inflater.inflate(R.layout.settings_item_delimiter, parent, false);
                return new DelimiterSettingsViewHolder(v);
            case GROUP:
                v = inflater.inflate(R.layout.settings_item_group, parent, false);
                return new GroupSettingsViewHolder(v);
            case INTENT:
                v = inflater.inflate(R.layout.settings_item_intent, parent, false);
                return new IntentSettingsViewHolder(v);
            case PROFILE_GROUP:
                v = inflater.inflate(R.layout.settings_item_profile_group, parent, false);
                return new ProfileGroupSettingsViewHolder(v);
            case PROFILE:
                v = inflater.inflate(R.layout.settings_item_profile, parent, false);
                return new ProfileSettingsViewHolder(v);
            default:
                throw new IllegalArgumentException("Unsupported settings item type");
        }
    }
}
