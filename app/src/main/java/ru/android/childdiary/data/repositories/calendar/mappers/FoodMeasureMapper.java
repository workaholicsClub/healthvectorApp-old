package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.core.FoodMeasureData;
import ru.android.childdiary.data.entities.calendar.events.core.FoodMeasureEntity;
import ru.android.childdiary.domain.interactors.calendar.events.core.FoodMeasure;

public class FoodMeasureMapper {
    public static FoodMeasure mapToPlainObject(@NonNull FoodMeasureData foodMeasureData) {
        return FoodMeasure.builder()
                .id(foodMeasureData.getId())
                .name(foodMeasureData.getName())
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
