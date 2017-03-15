package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;

import ru.android.childdiary.data.entities.calendar.events.standard.FoodMeasureData;
import ru.android.childdiary.data.entities.calendar.events.standard.FoodMeasureEntity;
import ru.android.childdiary.domain.interactors.calendar.FoodMeasure;

class FoodMeasureMapper {
    public static FoodMeasure mapToPlainObject(@NonNull FoodMeasureData foodMeasure) {
        return FoodMeasure.builder()
                .id(foodMeasure.getId())
                .name(foodMeasure.getName())
                .build();
    }

    public static FoodMeasureEntity mapToEntity(@NonNull FoodMeasure foodMeasure) {
        return updateEntityWithPlainObject(new FoodMeasureEntity(), foodMeasure);
    }

    public static FoodMeasureEntity updateEntityWithPlainObject(@NonNull FoodMeasureEntity to, @NonNull FoodMeasure from) {
        to.setName(from.getName());
        return to;
    }
}
