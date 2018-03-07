package ru.android.healthvector.data.db.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.healthvector.data.types.AchievementType;

public class AchievementTypeEnumConverter extends EnumOrdinalConverter<AchievementType> {
    public AchievementTypeEnumConverter() {
        super(AchievementType.class);
    }
}
