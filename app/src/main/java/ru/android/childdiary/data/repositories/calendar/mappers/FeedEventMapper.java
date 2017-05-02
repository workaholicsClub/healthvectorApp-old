package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.core.FoodData;
import ru.android.childdiary.data.entities.calendar.events.core.FoodEntity;
import ru.android.childdiary.data.entities.calendar.events.core.FoodMeasureData;
import ru.android.childdiary.data.entities.calendar.events.core.FoodMeasureEntity;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.core.RepeatParametersData;
import ru.android.childdiary.data.entities.calendar.events.standard.FeedEventData;
import ru.android.childdiary.data.entities.calendar.events.standard.FeedEventEntity;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.domain.interactors.calendar.events.core.Food;
import ru.android.childdiary.domain.interactors.calendar.events.core.FoodMeasure;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.child.Child;

public class FeedEventMapper {
    public static FeedEvent mapToPlainObject(@NonNull FeedEventData eventData) {
        MasterEventData masterEventData = eventData.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? null : ChildMapper.mapToPlainObject(childData);
        RepeatParametersData repeatParametersData = masterEventData.getRepeatParameters();
        RepeatParameters repeatParameters = repeatParametersData == null ? null : RepeatParametersMapper.mapToPlainObject(repeatParametersData);
        FoodMeasureData foodMeasureData = eventData.getFoodMeasure();
        FoodMeasure foodMeasure = foodMeasureData == null ? null : FoodMeasureMapper.mapToPlainObject(foodMeasureData);
        FoodData foodData = eventData.getFood();
        Food food = foodData == null ? null : FoodMapper.mapToPlainObject(foodData);
        return FeedEvent.builder()
                .id(eventData.getId())
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .dateTime(masterEventData.getDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .child(child)
                .repeatParameters(repeatParameters)
                .linearGroup(masterEventData.getLinearGroup())
                .feedType(eventData.getFeedType())
                .breast(eventData.getBreast())
                .leftDurationInMinutes(eventData.getLeftDurationInMinutes())
                .rightDurationInMinutes(eventData.getRightDurationInMinutes())
                .amount(eventData.getAmount())
                .amountMl(eventData.getAmountMl())
                .foodMeasure(foodMeasure)
                .food(food)
                .build();
    }

    public static FeedEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                              @NonNull FeedEvent feedEvent) {
        return mapToEntity(blockingEntityStore, feedEvent, null);
    }

    public static FeedEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                              @NonNull FeedEvent feedEvent,
                                              @Nullable MasterEvent masterEvent) {
        FeedEventEntity feedEventEntity;
        if (feedEvent.getId() == null) {
            feedEventEntity = new FeedEventEntity();
        } else {
            feedEventEntity = (FeedEventEntity) blockingEntityStore.findByKey(FeedEventEntity.class, feedEvent.getId());
        }
        fillNonReferencedFields(feedEventEntity, feedEvent);
        if (masterEvent != null) {
            MasterEventEntity masterEventEntity = (MasterEventEntity) blockingEntityStore.findByKey(MasterEventEntity.class, masterEvent.getMasterEventId());
            feedEventEntity.setMasterEvent(masterEventEntity);
        }
        FoodMeasure foodMeasure = feedEvent.getFoodMeasure();
        if (foodMeasure != null) {
            FoodMeasureEntity foodMeasureEntity = (FoodMeasureEntity) blockingEntityStore.findByKey(FoodMeasureEntity.class, foodMeasure.getId());
            feedEventEntity.setFoodMeasure(foodMeasureEntity);
        }
        Food food = feedEvent.getFood();
        if (food != null) {
            FoodEntity foodEntity = (FoodEntity) blockingEntityStore.findByKey(FoodEntity.class, food.getId());
            feedEventEntity.setFood(foodEntity);
        }
        return feedEventEntity;
    }

    private static void fillNonReferencedFields(@NonNull FeedEventEntity to, @NonNull FeedEvent from) {
        to.setFeedType(from.getFeedType());
        to.setBreast(from.getBreast());
        to.setLeftDurationInMinutes(from.getLeftDurationInMinutes());
        to.setRightDurationInMinutes(from.getRightDurationInMinutes());
        to.setAmount(from.getAmount());
        to.setAmountMl(from.getAmountMl());
    }
}
