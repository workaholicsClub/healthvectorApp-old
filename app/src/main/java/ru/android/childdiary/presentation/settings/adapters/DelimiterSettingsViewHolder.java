package ru.android.childdiary.presentation.settings.adapters;

import android.content.Context;
import android.view.View;

import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.settings.adapters.items.DelimiterSettingsItem;

public class DelimiterSettingsViewHolder extends BaseSettingsViewHolder<DelimiterSettingsItem> {
    public DelimiterSettingsViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void bindT(Context context, Sex sex, DelimiterSettingsItem item) {
    }
}
