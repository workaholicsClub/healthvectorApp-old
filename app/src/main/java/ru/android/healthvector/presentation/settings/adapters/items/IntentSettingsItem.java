package ru.android.healthvector.presentation.settings.adapters.items;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;

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
    Listener listener;
    @DrawableRes
    int iconRes;
    @NonNull
    String title;
    @Nullable
    String subtitle;
    boolean enabled;

    @Builder(toBuilder = true)
    private IntentSettingsItem(long id,
                               @NonNull Listener listener,
                               @DrawableRes int iconRes,
                               @NonNull String title,
                               @Nullable String subtitle,
                               boolean enabled) {
        super(id);
        this.listener = listener;
        this.iconRes = iconRes;
        this.title = title;
        this.subtitle = subtitle;
        this.enabled = enabled;
    }

    @Override
    public SettingsItemType getType() {
        return SettingsItemType.INTENT;
    }

    public interface Listener {
        void onClick(IntentSettingsItem item);
    }
}
