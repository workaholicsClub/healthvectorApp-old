package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.core.FoodData;
import ru.android.childdiary.data.entities.calendar.events.core.FoodEntity;
import ru.android.childdiary.domain.interactors.calendar.events.core.Food;

public class FoodMapper {
    public static Food mapToPlainObject(@NonNull FoodData foodData) {
        return Food.builder()
                .id(foodData.getId())
                .name(foodData.getName())
                .build();
    }

    public static FoodEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                         @NonNull Food food) {
        FoodEntity foodEntity;
        if (food.getId() == null) {
            foodEntity = new FoodEntity();
        } else {
            foodEntity = (FoodEntity) blockingEntityStore.findByKey(FoodEntity.class, food.getId());
        }
        fillNonReferencedFields(foodEntity, food);
        return foodEntity;
    }

    private static void fillNonReferencedFields(@NonNull FoodEntity to, @NonNull Food from) {
        to.setName(from.getName());
    }
}
