package ru.android.childdiary.presentation.settings.adapters.items;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GroupSettingsItem extends BaseSettingsItem {
    @NonNull
    String title;

    @Builder
    private GroupSettingsItem(long id, @NonNull String title) {
        super(id);
        this.title = title;
    }

    @Override
    public SettingsItemType getType() {
        return SettingsItemType.GROUP;
    }
}
