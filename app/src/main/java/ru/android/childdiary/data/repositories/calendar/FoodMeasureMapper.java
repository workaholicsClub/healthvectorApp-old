package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.FoodMeasureData;
import ru.android.childdiary.data.entities.calendar.FoodMeasureEntity;
import ru.android.childdiary.domain.interactors.calendar.FoodMeasure;

class FoodMeasureMapper {
    public static FoodMeasure mapToPlainObject(@NonNull FoodMeasureData foodMeasure) {
        return FoodMeasure.builder()
                .id(foodMeasure.getId())
                .name(foodMeasure.getName())
                .build();
    }

    public static FoodMeasureEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                                @NonNull FoodMeasure foodMeasure) {
        FoodMeasureEntity foodMeasureEntity;
        if (foodMeasure.getId() == null) {
            foodMeasureEntity = new FoodMeasureEntity();
        } else {
            foodMeasureEntity = (FoodMeasureEntity) blockingEntityStore.findByKey(FoodMeasureEntity.class, foodMeasure.getId());
        }
        fillNonReferencedFields(foodMeasureEntity, foodMeasure);
        return foodMeasureEntity;
    }

    private static void fillNonReferencedFields(@NonNull FoodMeasureEntity to, @NonNull FoodMeasure from) {
        to.setName(from.getName());
    }
}
