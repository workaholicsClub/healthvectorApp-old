package ru.android.childdiary.presentation.settings.adapters.items;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DelimiterSettingsItem extends BaseSettingsItem {
    @Builder
    private DelimiterSettingsItem(long id) {
        super(id);
    }

    @Override
    public SettingsItemType getType() {
        return SettingsItemType.DELIMITER;
    }
}
