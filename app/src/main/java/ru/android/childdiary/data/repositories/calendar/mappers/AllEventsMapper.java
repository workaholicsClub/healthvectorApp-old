package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import io.requery.query.Tuple;
import ru.android.childdiary.data.entities.calendar.events.DoctorVisitEventEntity;
import ru.android.childdiary.data.entities.calendar.events.core.FoodEntity;
import ru.android.childdiary.data.entities.calendar.events.core.FoodMeasureEntity;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.core.RepeatParametersEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.DiaperEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.FeedEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.OtherEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.PumpEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.SleepEventEntity;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.data.entities.medical.DoctorVisitEntity;
import ru.android.childdiary.data.entities.medical.MedicineTakingEntity;
import ru.android.childdiary.data.entities.medical.core.DoctorEntity;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.events.DoctorVisitEvent;
import ru.android.childdiary.domain.interactors.calendar.events.MedicineTakingEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.Food;
import ru.android.childdiary.domain.interactors.calendar.events.core.FoodMeasure;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;

public class AllEventsMapper {
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
            case DOCTOR_VISIT:
                return mapToDoctorVisitEvent(data);
            case MEDICINE_TAKING:
                return mapToMedicineTakingEvent(data);
        }
        throw new IllegalStateException("Unknown event type");
    }

    private static DiaperEvent mapToDiaperEvent(@NonNull Tuple data) {
        return DiaperEvent.builder()
                .id(data.get(SleepEventEntity.ID.as("diaper_event_id")))
                .masterEventId(data.get(MasterEventEntity.ID))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE))
                .dateTime(data.get(MasterEventEntity.DATE_TIME))
                .notifyTimeInMinutes(data.get(MasterEventEntity.NOTIFY_TIME_IN_MINUTES))
                .note(data.get(MasterEventEntity.NOTE))
                .isDone(data.get(MasterEventEntity.DONE))
                .child(mapToChild(data))
                .repeatParameters(mapToRepeatParameters(data))
                .linearGroup(data.get(MasterEventEntity.LINEAR_GROUP))
                .diaperState(data.get(DiaperEventEntity.DIAPER_STATE))
                .build();
    }

    private static FeedEvent mapToFeedEvent(@NonNull Tuple data) {
        return FeedEvent.builder()
                .id(data.get(SleepEventEntity.ID.as("feed_event_id")))
                .masterEventId(data.get(MasterEventEntity.ID))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE))
                .dateTime(data.get(MasterEventEntity.DATE_TIME))
                .notifyTimeInMinutes(data.get(MasterEventEntity.NOTIFY_TIME_IN_MINUTES))
                .note(data.get(MasterEventEntity.NOTE))
                .isDone(data.get(MasterEventEntity.DONE))
                .child(mapToChild(data))
                .repeatParameters(mapToRepeatParameters(data))
                .linearGroup(data.get(MasterEventEntity.LINEAR_GROUP))
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
                .dateTime(data.get(MasterEventEntity.DATE_TIME))
                .notifyTimeInMinutes(data.get(MasterEventEntity.NOTIFY_TIME_IN_MINUTES))
                .note(data.get(MasterEventEntity.NOTE))
                .isDone(data.get(MasterEventEntity.DONE))
                .child(mapToChild(data))
                .repeatParameters(mapToRepeatParameters(data))
                .linearGroup(data.get(MasterEventEntity.LINEAR_GROUP))
                .name(data.get(OtherEventEntity.NAME.as("other_event_name")))
                .finishDateTime(data.get(OtherEventEntity.FINISH_DATE_TIME.as("other_event_finish_date_time")))
                .build();
    }

    private static PumpEvent mapToPumpEvent(@NonNull Tuple data) {
        return PumpEvent.builder()
                .id(data.get(SleepEventEntity.ID.as("pump_event_id")))
                .masterEventId(data.get(MasterEventEntity.ID))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE))
                .dateTime(data.get(MasterEventEntity.DATE_TIME))
                .notifyTimeInMinutes(data.get(MasterEventEntity.NOTIFY_TIME_IN_MINUTES))
                .note(data.get(MasterEventEntity.NOTE))
                .isDone(data.get(MasterEventEntity.DONE))
                .child(mapToChild(data))
                .repeatParameters(mapToRepeatParameters(data))
                .linearGroup(data.get(MasterEventEntity.LINEAR_GROUP))
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
                .dateTime(data.get(MasterEventEntity.DATE_TIME))
                .notifyTimeInMinutes(data.get(MasterEventEntity.NOTIFY_TIME_IN_MINUTES))
                .note(data.get(MasterEventEntity.NOTE))
                .isDone(data.get(MasterEventEntity.DONE))
                .child(mapToChild(data))
                .repeatParameters(mapToRepeatParameters(data))
                .linearGroup(data.get(MasterEventEntity.LINEAR_GROUP))
                .finishDateTime(data.get(SleepEventEntity.FINISH_DATE_TIME.as("sleep_event_finish_date_time")))
                .isTimerStarted(data.get(SleepEventEntity.TIMER_STARTED))
                .build();
    }

    private static Child mapToChild(@NonNull Tuple data) {
        return Child.builder()
                .id(data.get(ChildEntity.ID.as("child_id")))
                .name(data.get(ChildEntity.NAME.as("child_name")))
                .birthDate(data.get(ChildEntity.BIRTH_DATE))
                .birthTime(data.get(ChildEntity.BIRTH_TIME))
                .sex(data.get(ChildEntity.SEX))
                .imageFileName(data.get(ChildEntity.IMAGE_FILE_NAME))
                .birthHeight(data.get(ChildEntity.BIRTH_HEIGHT))
                .birthWeight(data.get(ChildEntity.BIRTH_WEIGHT))
                .build();
    }

    public static FoodMeasure mapToFoodMeasure(@NonNull Tuple data) {
        return FoodMeasure.builder()
                .id(data.get(FoodMeasureEntity.ID.as("food_measure_id")))
                .name(data.get(FoodMeasureEntity.NAME.as("food_measure_name")))
                .build();
    }

    public static Food mapToFood(@NonNull Tuple data) {
        return Food.builder()
                .id(data.get(FoodEntity.ID.as("food_id")))
                .name(data.get(FoodEntity.NAME.as("food_name")))
                .build();
    }

    public static DoctorVisitEvent mapToDoctorVisitEvent(@NonNull Tuple data) {
        return DoctorVisitEvent.builder()
                .id(data.get(DoctorVisitEventEntity.ID.as("doctor_visit_event_id")))
                .masterEventId(data.get(MasterEventEntity.ID))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE))
                .dateTime(data.get(MasterEventEntity.DATE_TIME))
                .notifyTimeInMinutes(data.get(MasterEventEntity.NOTIFY_TIME_IN_MINUTES))
                .note(data.get(MasterEventEntity.NOTE))
                .isDone(data.get(MasterEventEntity.DONE))
                .child(mapToChild(data))
                .repeatParameters(mapToRepeatParameters(data))
                .linearGroup(data.get(MasterEventEntity.LINEAR_GROUP))
                .doctorVisit(mapToDoctorVisit(data))
                .build();
    }

    public static DoctorVisit mapToDoctorVisit(@NonNull Tuple data) {
        return DoctorVisit.builder()
                .id(data.get(DoctorVisitEntity.ID.as("doctor_visit_id")))
                .doctor(mapToDoctor(data))
                .name(data.get(DoctorVisitEntity.NAME.as("doctor_visit_name")))
                .durationInMinutes(data.get(DoctorVisitEntity.DURATION_IN_MINUTES.as("doctor_visit_duration_in_minutes")))
                .dateTime(data.get(DoctorVisitEntity.DATE_TIME.as("doctor_visit_date_time")))
                .notifyTimeInMinutes(data.get(DoctorVisitEntity.NOTIFY_TIME_IN_MINUTES.as("doctor_visit_notify_time_in_minutes")))
                .note(data.get(DoctorVisitEntity.NOTE.as("doctor_visit_note")))
                .imageFileName(data.get(DoctorVisitEntity.IMAGE_FILE_NAME.as("doctor_visit_image_file_name")))
                .build();
    }

    public static Doctor mapToDoctor(@NonNull Tuple data) {
        return Doctor.builder()
                .id(data.get(DoctorEntity.ID.as("doctor_id")))
                .name(data.get(DoctorEntity.NAME.as("doctor_name")))
                .build();
    }

    public static MedicineTakingEvent mapToMedicineTakingEvent(@NonNull Tuple data) {
        return MedicineTakingEvent.builder()
                .id(data.get(DoctorVisitEventEntity.ID.as("medicine_taking_event_id")))
                .masterEventId(data.get(MasterEventEntity.ID))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE))
                .dateTime(data.get(MasterEventEntity.DATE_TIME))
                .notifyTimeInMinutes(data.get(MasterEventEntity.NOTIFY_TIME_IN_MINUTES))
                .note(data.get(MasterEventEntity.NOTE))
                .isDone(data.get(MasterEventEntity.DONE))
                .child(mapToChild(data))
                .repeatParameters(mapToRepeatParameters(data))
                .linearGroup(data.get(MasterEventEntity.LINEAR_GROUP))
                .medicineTaking(mapToMedicineTaking(data))
                .build();
    }

    public static MedicineTaking mapToMedicineTaking(@NonNull Tuple data) {
        return MedicineTaking.builder()
                .id(data.get(MedicineTakingEntity.ID.as("medicine_taking_id")))
                .medicine(mapToMedicine(data))
                .dateTime(data.get(MedicineTakingEntity.DATE_TIME.as("medicine_taking_date_time")))
                .notifyTimeInMinutes(data.get(MedicineTakingEntity.NOTIFY_TIME_IN_MINUTES.as("medicine_taking_notify_time_in_minutes")))
                .note(data.get(MedicineTakingEntity.NOTE.as("medicine_taking_note")))
                .imageFileName(data.get(MedicineTakingEntity.IMAGE_FILE_NAME.as("medicine_taking_image_file_name")))
                .build();
    }

    public static Medicine mapToMedicine(@NonNull Tuple data) {
        return Medicine.builder()
                .id(data.get(DoctorEntity.ID.as("medicine_id")))
                .name(data.get(DoctorEntity.NAME.as("medicine_name")))
                .build();
    }

    public static RepeatParameters mapToRepeatParameters(@NonNull Tuple data) {
        return RepeatParameters.builder()
                .id(data.get(RepeatParametersEntity.ID.as("repeat_parameters_id")))
                .dateTimeFrom(data.get(RepeatParametersEntity.DATE_TIME_FROM.as("repeat_parameters_date_time_from")))
                .periodicityInMinutes(data.get(RepeatParametersEntity.PERIODICITY_IN_MINUTES))
                .lengthInMinutes(data.get(RepeatParametersEntity.LENGTH_IN_MINUTES))
                .linearGroups(data.get(RepeatParametersEntity.LINEAR_GROUPS))
                .build();
    }
}
