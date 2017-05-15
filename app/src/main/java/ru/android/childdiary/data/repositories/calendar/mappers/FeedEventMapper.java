package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.core.FoodData;
import ru.android.childdiary.data.entities.calendar.events.core.FoodEntity;
import ru.android.childdiary.data.entities.calendar.events.core.FoodMeasureData;
import ru.android.childdiary.data.entities.calendar.events.core.FoodMeasureEntity;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.FeedEventData;
import ru.android.childdiary.data.entities.calendar.events.standard.FeedEventEntity;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.core.RepeatParametersData;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.interactors.calendar.events.core.Food;
import ru.android.childdiary.domain.interactors.calendar.events.core.FoodMeasure;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;

public class FeedEventMapper implements EntityMapper<FeedEventData, FeedEventEntity, FeedEvent> {
    private final ChildMapper childMapper;
    private final RepeatParametersMapper repeatParametersMapper;
    private final FoodMeasureMapper foodMeasureMapper;
    private final FoodMapper foodMapper;

    @Inject
    public FeedEventMapper(ChildMapper childMapper,
                           RepeatParametersMapper repeatParametersMapper,
                           FoodMeasureMapper foodMeasureMapper,
                           FoodMapper foodMapper) {
        this.childMapper = childMapper;
        this.repeatParametersMapper = repeatParametersMapper;
        this.foodMeasureMapper = foodMeasureMapper;
        this.foodMapper = foodMapper;
    }

    @Override
    public FeedEvent mapToPlainObject(@NonNull FeedEventData feedEventData) {
        MasterEventData masterEventData = feedEventData.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? null : childMapper.mapToPlainObject(childData);
        RepeatParametersData repeatParametersData = masterEventData.getRepeatParameters();
        RepeatParameters repeatParameters = repeatParametersData == null ? null : repeatParametersMapper.mapToPlainObject(repeatParametersData);
        FoodMeasureData foodMeasureData = feedEventData.getFoodMeasure();
        FoodMeasure foodMeasure = foodMeasureData == null ? null : foodMeasureMapper.mapToPlainObject(foodMeasureData);
        FoodData foodData = feedEventData.getFood();
        Food food = foodData == null ? null : foodMapper.mapToPlainObject(foodData);
        return FeedEvent.builder()
                .id(feedEventData.getId())
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .dateTime(masterEventData.getDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .child(child)
                .repeatParameters(repeatParameters)
                .linearGroup(masterEventData.getLinearGroup())
                .feedType(feedEventData.getFeedType())
                .breast(feedEventData.getBreast())
                .leftDurationInMinutes(feedEventData.getLeftDurationInMinutes())
                .rightDurationInMinutes(feedEventData.getRightDurationInMinutes())
                .amount(feedEventData.getAmount())
                .amountMl(feedEventData.getAmountMl())
                .foodMeasure(foodMeasure)
                .food(food)
                .build();
    }

    @Override
    public FeedEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                       @NonNull FeedEvent feedEvent) {
        FeedEventEntity feedEventEntity;
        if (feedEvent.getId() == null) {
            feedEventEntity = new FeedEventEntity();
        } else {
            feedEventEntity = (FeedEventEntity) blockingEntityStore.findByKey(FeedEventEntity.class, feedEvent.getId());
        }
        fillNonReferencedFields(feedEventEntity, feedEvent);

        MasterEventEntity masterEventEntity = (MasterEventEntity) blockingEntityStore.findByKey(MasterEventEntity.class, feedEvent.getMasterEventId());
        feedEventEntity.setMasterEvent(masterEventEntity);

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

    @Override
    public void fillNonReferencedFields(@NonNull FeedEventEntity to, @NonNull FeedEvent from) {
        to.setFeedType(from.getFeedType());
        to.setBreast(from.getBreast());
        to.setLeftDurationInMinutes(from.getLeftDurationInMinutes());
        to.setRightDurationInMinutes(from.getRightDurationInMinutes());
        to.setAmount(from.getAmount());
        to.setAmountMl(from.getAmountMl());
    }
}
