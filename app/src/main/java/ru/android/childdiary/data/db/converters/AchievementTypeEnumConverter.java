package ru.android.childdiary.data.db.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.childdiary.data.types.AchievementType;

public class AchievementTypeEnumConverter extends EnumOrdinalConverter<AchievementType> {
    public AchievementTypeEnumConverter() {
        super(AchievementType.class);
    }
}
