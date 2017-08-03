package ru.android.childdiary.presentation.settings.adapters.items;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProfileGroupSettingsItem extends BaseSettingsItem {
    @NonNull
    Listener listener;
    @NonNull
    String title;

    @Builder
    private ProfileGroupSettingsItem(long id, @NonNull Listener listener, @NonNull String title) {
        super(id);
        this.listener = listener;
        this.title = title;
    }

    @Override
    public SettingsItemType getType() {
        return SettingsItemType.PROFILE_GROUP;
    }

    public interface Listener {
        void addProfile(ProfileGroupSettingsItem item);
    }
}
