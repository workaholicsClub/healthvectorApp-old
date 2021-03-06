package ru.android.healthvector.presentation.settings.adapters.items;

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
    long id;

    public abstract SettingsItemType getType();
}
