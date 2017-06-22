package ru.android.childdiary.presentation.settings.adapters.items;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString
@EqualsAndHashCode
@AllArgsConstructor(suppressConstructorProperties = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@Getter
public abstract class BaseSettingsItem {
    int id;

    public abstract SettingsItemType getType();
}
