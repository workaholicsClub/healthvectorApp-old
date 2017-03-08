package ru.android.childdiary.data.converters;

import io.requery.converter.EnumOrdinalConverter;
import ru.android.childdiary.data.types.FoodMeasure;

public class FoodMeasureEnumConverter extends EnumOrdinalConverter<FoodMeasure> {
    public FoodMeasureEnumConverter() {
        super(FoodMeasure.class);
    }
}
