package ru.android.childdiary.presentation.settings.adapters.items;

import android.content.Intent;
import android.support.annotation.DrawableRes;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class IntentSettingsItem extends BaseSettingsItem {
    @NonNull
    Intent intent;
    @DrawableRes
    int iconRes;
    @NonNull
    String title;

    @Builder
    private IntentSettingsItem(int id,
                               @NonNull Intent intent,
                               @DrawableRes int iconRes,
                               @NonNull String title) {
        super(id);
        this.intent = intent;
        this.iconRes = iconRes;
        this.title = title;
    }

    @Override
    public SettingsItemType getType() {
        return SettingsItemType.INTENT;
    }
}
