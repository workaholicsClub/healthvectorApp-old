package ru.android.childdiary.presentation.settings.adapters.items;

import android.graphics.drawable.Drawable;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.domain.child.data.Child;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProfileSettingsItem extends BaseSettingsItem {
    @NonNull
    Listener listener;
    @NonNull
    Child child;
    @NonNull
    Drawable icon;
    @NonNull
    String title;

    @Builder
    private ProfileSettingsItem(long id,
                                @NonNull Listener listener,
                                @NonNull Child child,
                                @NonNull Drawable icon,
                                @NonNull String title) {
        super(id);
        this.listener = listener;
        this.child = child;
        this.icon = icon;
        this.title = title;
    }

    @Override
    public SettingsItemType getType() {
        return SettingsItemType.PROFILE;
    }

    public interface Listener {
        void reviewProfile(ProfileSettingsItem item);

        void deleteProfile(ProfileSettingsItem item);
    }
}
