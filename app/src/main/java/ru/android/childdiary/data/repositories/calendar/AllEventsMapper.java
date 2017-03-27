package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;

import io.requery.query.Tuple;
import ru.android.childdiary.data.entities.calendar.FoodEntity;
import ru.android.childdiary.data.entities.calendar.FoodMeasureEntity;
import ru.android.childdiary.data.entities.calendar.events.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.DiaperEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.FeedEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.OtherEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.PumpEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.SleepEventEntity;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.Food;
import ru.android.childdiary.domain.interactors.calendar.FoodMeasure;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.child.Child;

class AllEventsMapper {
    public static MasterEvent mapToPlainObject(@NonNull Tuple data) {
        EventType eventType = data.get(MasterEventEntity.EVENT_TYPE);
        if (eventType == null) {
            throw new IllegalStateException("Unknown event type");
        }
        switch (eventType) {
            case DIAPER:
                return mapToDiaperEvent(data);
            case FEED:
                return mapToFeedEvent(data);
            case OTHER:
                return mapToOtherEvent(data);
            case PUMP:
                return mapToPumpEvent(data);
            case SLEEP:
                return mapToSleepEvent(data);
        }
        throw new IllegalStateException("Unknown event type");
    }

    private static DiaperEvent mapToDiaperEvent(@NonNull Tuple data) {
        DiaperEvent.builder()
                .id(data.get(SleepEventEntity.ID.as("diaper_event_id")))
                .masterEventId(data.get(MasterEventEntity.ID))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE))
                .description(data.get(MasterEventEntity.DESCRIPTION))
                .dateTime(data.get(MasterEventEntity.DATE_TIME))
                .notifyTimeInMinutes(data.get(MasterEventEntity.NOTIFY_TIME_IN_MINUTES))
                .note(data.get(MasterEventEntity.NOTE))
                .isDone(data.get(MasterEventEntity.DONE))
                .isDeleted(data.get(MasterEventEntity.DELETED))
                .child(mapToChild(data))
                .diaperState(data.get(DiaperEventEntity.DIAPER_STATE))
                .build();
    }

    private static FeedEvent mapToFeedEvent(@NonNull Tuple data) {
        return FeedEvent.builder()
                .id(data.get(SleepEventEntity.ID.as("feed_event_id")))
                .masterEventId(data.get(MasterEventEntity.ID))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE))
                .description(data.get(MasterEventEntity.DESCRIPTION))
                .dateTime(data.get(MasterEventEntity.DATE_TIME))
                .notifyTimeInMinutes(data.get(MasterEventEntity.NOTIFY_TIME_IN_MINUTES))
                .note(data.get(MasterEventEntity.NOTE))
                .isDone(data.get(MasterEventEntity.DONE))
                .isDeleted(data.get(MasterEventEntity.DELETED))
                .child(mapToChild(data))
                .feedType(data.get(FeedEventEntity.FEED_TYPE))
                .breast(data.get(FeedEventEntity.BREAST.as("feed_event_breast")))
                .leftDurationInMinutes(data.get(FeedEventEntity.LEFT_DURATION_IN_MINUTES))
                .rightDurationInMinutes(data.get(FeedEventEntity.RIGHT_DURATION_IN_MINUTES))
                .amount(data.get(FeedEventEntity.AMOUNT))
                .amountMl(data.get(FeedEventEntity.AMOUNT_ML))
                .foodMeasure(mapToFoodMeasure(data))
                .food(mapToFood(data))
                .build();
    }

    private static OtherEvent mapToOtherEvent(@NonNull Tuple data) {
        return OtherEvent.builder()
                .id(data.get(SleepEventEntity.ID.as("other_event_id")))
                .masterEventId(data.get(MasterEventEntity.ID))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE))
                .description(data.get(MasterEventEntity.DESCRIPTION))
                .dateTime(data.get(MasterEventEntity.DATE_TIME))
                .notifyTimeInMinutes(data.get(MasterEventEntity.NOTIFY_TIME_IN_MINUTES))
                .note(data.get(MasterEventEntity.NOTE))
                .isDone(data.get(MasterEventEntity.DONE))
                .isDeleted(data.get(MasterEventEntity.DELETED))
                .child(mapToChild(data))
                .name(data.get(OtherEventEntity.NAME.as("other_event_name")))
                .finishDateTime(data.get(OtherEventEntity.FINISH_DATE_TIME.as("other_event_finish_date_time")))
                .build();
    }

    private static PumpEvent mapToPumpEvent(@NonNull Tuple data) {
        return PumpEvent.builder()
                .id(data.get(SleepEventEntity.ID.as("pump_event_id")))
                .masterEventId(data.get(MasterEventEntity.ID))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE))
                .description(data.get(MasterEventEntity.DESCRIPTION))
                .dateTime(data.get(MasterEventEntity.DATE_TIME))
                .notifyTimeInMinutes(data.get(MasterEventEntity.NOTIFY_TIME_IN_MINUTES))
                .note(data.get(MasterEventEntity.NOTE))
                .isDone(data.get(MasterEventEntity.DONE))
                .isDeleted(data.get(MasterEventEntity.DELETED))
                .child(mapToChild(data))
                .breast(data.get(PumpEventEntity.BREAST.as("pump_event_breast")))
                .leftAmountMl(data.get(PumpEventEntity.LEFT_AMOUNT_ML))
                .rightAmountMl(data.get(PumpEventEntity.RIGHT_AMOUNT_ML))
                .build();
    }

    private static SleepEvent mapToSleepEvent(@NonNull Tuple data) {
        return SleepEvent.builder()
                .id(data.get(SleepEventEntity.ID.as("sleep_event_id")))
                .masterEventId(data.get(MasterEventEntity.ID))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE))
                .description(data.get(MasterEventEntity.DESCRIPTION))
                .dateTime(data.get(MasterEventEntity.DATE_TIME))
                .notifyTimeInMinutes(data.get(MasterEventEntity.NOTIFY_TIME_IN_MINUTES))
                .note(data.get(MasterEventEntity.NOTE))
                .isDone(data.get(MasterEventEntity.DONE))
                .isDeleted(data.get(MasterEventEntity.DELETED))
                .child(mapToChild(data))
                .finishDateTime(data.get(SleepEventEntity.FINISH_DATE_TIME.as("sleep_event_finish_date_time")))
                .isTimerStarted(data.get(SleepEventEntity.TIMER_STARTED))
                .build();
    }

    private static Child mapToChild(@NonNull Tuple childData) {
        return Child.builder()
                .id(childData.get(ChildEntity.ID.as("child_id")))
                .name(childData.get(ChildEntity.NAME.as("child_name")))
                .birthDate(childData.get(ChildEntity.BIRTH_DATE))
                .birthTime(childData.get(ChildEntity.BIRTH_TIME))
                .sex(childData.get(ChildEntity.SEX))
                .imageFileName(childData.get(ChildEntity.IMAGE_FILE_NAME))
                .birthHeight(childData.get(ChildEntity.BIRTH_HEIGHT))
                .birthWeight(childData.get(ChildEntity.BIRTH_WEIGHT))
                .build();
    }

    public static FoodMeasure mapToFoodMeasure(@NonNull Tuple foodMeasureData) {
        return FoodMeasure.builder()
                .id(foodMeasureData.get(FoodMeasureEntity.ID.as("food_measure_id")))
                .name(foodMeasureData.get(FoodMeasureEntity.NAME.as("food_measure_name")))
                .build();
    }

    public static Food mapToFood(@NonNull Tuple foodData) {
        return Food.builder()
                .id(foodData.get(FoodEntity.ID.as("food_id")))
                .name(foodData.get(FoodEntity.NAME.as("food_name")))
                .build();
    }
}
