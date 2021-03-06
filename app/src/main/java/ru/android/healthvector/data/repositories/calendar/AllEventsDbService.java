package ru.android.healthvector.data.repositories.calendar;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.meta.QueryAttribute;
import io.requery.query.Expression;
import io.requery.query.Tuple;
import io.requery.query.WhereAndOr;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveResult;
import ru.android.healthvector.data.db.DbUtils;
import ru.android.healthvector.data.db.entities.calendar.DoctorVisitEventEntity;
import ru.android.healthvector.data.db.entities.calendar.ExerciseEventEntity;
import ru.android.healthvector.data.db.entities.calendar.MedicineTakingEventEntity;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventEntity;
import ru.android.healthvector.data.db.entities.calendar.standard.DiaperEventEntity;
import ru.android.healthvector.data.db.entities.calendar.standard.FeedEventEntity;
import ru.android.healthvector.data.db.entities.calendar.standard.OtherEventEntity;
import ru.android.healthvector.data.db.entities.calendar.standard.PumpEventEntity;
import ru.android.healthvector.data.db.entities.calendar.standard.SleepEventEntity;
import ru.android.healthvector.data.db.entities.child.ChildEntity;
import ru.android.healthvector.data.db.entities.dictionaries.DoctorEntity;
import ru.android.healthvector.data.db.entities.dictionaries.FoodEntity;
import ru.android.healthvector.data.db.entities.dictionaries.MedicineEntity;
import ru.android.healthvector.data.db.entities.exercises.ConcreteExerciseEntity;
import ru.android.healthvector.data.db.entities.exercises.ExerciseEntity;
import ru.android.healthvector.data.db.entities.medical.DoctorVisitEntity;
import ru.android.healthvector.data.db.entities.medical.MedicineTakingEntity;
import ru.android.healthvector.data.repositories.core.mappers.EntityMapper;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.domain.calendar.data.DoctorVisitEvent;
import ru.android.healthvector.domain.calendar.data.ExerciseEvent;
import ru.android.healthvector.domain.calendar.data.MedicineTakingEvent;
import ru.android.healthvector.domain.calendar.data.core.MasterEvent;
import ru.android.healthvector.domain.calendar.data.standard.DiaperEvent;
import ru.android.healthvector.domain.calendar.data.standard.FeedEvent;
import ru.android.healthvector.domain.calendar.data.standard.OtherEvent;
import ru.android.healthvector.domain.calendar.data.standard.PumpEvent;
import ru.android.healthvector.domain.calendar.data.standard.SleepEvent;
import ru.android.healthvector.domain.calendar.requests.GetEventsRequest;
import ru.android.healthvector.domain.calendar.requests.GetEventsResponse;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.dictionaries.doctors.data.Doctor;
import ru.android.healthvector.domain.dictionaries.food.data.Food;
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;
import ru.android.healthvector.domain.exercises.data.ConcreteExercise;
import ru.android.healthvector.domain.exercises.data.Exercise;
import ru.android.healthvector.domain.medical.data.DoctorVisit;
import ru.android.healthvector.domain.medical.data.MedicineTaking;
import ru.android.healthvector.utils.strings.DateUtils;

@Singleton
public class AllEventsDbService implements EntityMapper<Tuple, Tuple, MasterEvent> {
    // master event
    private static final String MASTER_EVENT_ENTITY_ID = "MASTER_EVENT_ENTITY_ID";
    private static final String MASTER_EVENT_ENTITY_EVENT_TYPE = "MASTER_EVENT_ENTITY_EVENT_TYPE";
    private static final String MASTER_EVENT_ENTITY_DATE_TIME = "MASTER_EVENT_ENTITY_DATE_TIME";
    private static final String MASTER_EVENT_ENTITY_NOTIFY_DATE_TIME = "MASTER_EVENT_ENTITY_NOTIFY_DATE_TIME";
    private static final String MASTER_EVENT_ENTITY_NOTIFY_TIME_IN_MINUTES = "MASTER_EVENT_ENTITY_NOTIFY_TIME_IN_MINUTES";
    private static final String MASTER_EVENT_ENTITY_NOTE = "MASTER_EVENT_ENTITY_NOTE";
    private static final String MASTER_EVENT_ENTITY_DONE = "MASTER_EVENT_ENTITY_DONE";
    private static final String MASTER_EVENT_ENTITY_LINEAR_GROUP = "MASTER_EVENT_ENTITY_LINEAR_GROUP";
    // child
    private static final String CHILD_ENTITY_ID = "CHILD_ENTITY_ID";
    // diaper event
    private static final String DIAPER_EVENT_ENTITY_ID = "DIAPER_EVENT_ENTITY_ID";
    private static final String DIAPER_EVENT_ENTITY_DIAPER_STATE = "DIAPER_EVENT_ENTITY_DIAPER_STATE";
    // feed event
    private static final String FEED_EVENT_ENTITY_ID = "FEED_EVENT_ENTITY_ID";
    private static final String FEED_EVENT_ENTITY_FEED_TYPE = "FEED_EVENT_ENTITY_FEED_TYPE";
    private static final String FEED_EVENT_ENTITY_BREAST = "FEED_EVENT_ENTITY_BREAST";
    // food
    private static final String FOOD_ENTITY_NAME_EN = "FOOD_ENTITY_NAME_EN";
    private static final String FOOD_ENTITY_NAME_RU = "FOOD_ENTITY_NAME_RU";
    private static final String FOOD_ENTITY_NAME_USER = "FOOD_ENTITY_NAME_USER";
    // other event
    private static final String OTHER_EVENT_ENTITY_ID = "OTHER_EVENT_ENTITY_ID";
    private static final String OTHER_EVENT_ENTITY_NAME = "OTHER_EVENT_ENTITY_NAME";
    // pump event
    private static final String PUMP_EVENT_ENTITY_ID = "PUMP_EVENT_ENTITY_ID";
    private static final String PUMP_EVENT_ENTITY_BREAST = "PUMP_EVENT_ENTITY_BREAST";
    // sleep event
    private static final String SLEEP_EVENT_ENTITY_ID = "SLEEP_EVENT_ENTITY_ID";
    private static final String SLEEP_EVENT_ENTITY_FINISH_DATE_TIME = "SLEEP_EVENT_ENTITY_FINISH_DATE_TIME";
    private static final String SLEEP_EVENT_ENTITY_TIMER_STARTED = "SLEEP_EVENT_ENTITY_TIMER_STARTED";
    // doctor visit event
    private static final String DOCTOR_VISIT_EVENT_ENTITY_ID = "DOCTOR_VISIT_EVENT_ENTITY_ID";
    private static final String DOCTOR_VISIT_EVENT_ENTITY_NAME = "DOCTOR_VISIT_EVENT_ENTITY_NAME";
    private static final String DOCTOR_VISIT_EVENT_ENTITY_IMAGE_FILE_NAME = "DOCTOR_VISIT_EVENT_ENTITY_IMAGE_FILE_NAME";
    // doctor visit
    private static final String DOCTOR_VISIT_ENTITY_ID = "DOCTOR_VISIT_ENTITY_ID";
    // doctor
    private static final String DOCTOR_ENTITY_NAME_EN = "DOCTOR_ENTITY_NAME_EN";
    private static final String DOCTOR_ENTITY_NAME_RU = "DOCTOR_ENTITY_NAME_RU";
    private static final String DOCTOR_ENTITY_NAME_USER = "DOCTOR_ENTITY_NAME_USER";
    // medicine taking event
    private static final String MEDICINE_TAKING_EVENT_ENTITY_ID = "MEDICINE_TAKING_EVENT_ENTITY_ID";
    private static final String MEDICINE_TAKING_EVENT_ENTITY_IMAGE_FILE_NAME = "MEDICINE_TAKING_EVENT_ENTITY_IMAGE_FILE_NAME";
    // medicine taking
    private static final String MEDICINE_TAKING_ENTITY_ID = "MEDICINE_TAKING_ENTITY_ID";
    // medicine
    private static final String MEDICINE_ENTITY_NAME_EN = "MEDICINE_ENTITY_NAME_EN";
    private static final String MEDICINE_ENTITY_NAME_RU = "MEDICINE_ENTITY_NAME_RU";
    private static final String MEDICINE_ENTITY_NAME_USER = "MEDICINE_ENTITY_NAME_USER";
    // exercise event
    private static final String EXERCISE_EVENT_ENTITY_ID = "EXERCISE_EVENT_ENTITY_ID";
    private static final String EXERCISE_EVENT_ENTITY_NAME = "EXERCISE_EVENT_ENTITY_NAME";
    private static final String EXERCISE_EVENT_ENTITY_IMAGE_FILE_NAME = "EXERCISE_EVENT_ENTITY_IMAGE_FILE_NAME";
    // concrete exercise
    private static final String CONCRETE_EXERCISE_ENTITY_ID = "CONCRETE_EXERCISE_ENTITY_ID";
    // exercise
    private static final String EXERCISE_ENTITY_NAME_EN = "EXERCISE_ENTITY_NAME_EN";
    private static final String EXERCISE_ENTITY_NAME_RU = "EXERCISE_ENTITY_NAME_RU";

    private final static Expression[] EXPRESSIONS = new Expression[]{
            // master event
            MasterEventEntity.ID.as(MASTER_EVENT_ENTITY_ID),
            MasterEventEntity.EVENT_TYPE.as(MASTER_EVENT_ENTITY_EVENT_TYPE),
            MasterEventEntity.DATE_TIME.as(MASTER_EVENT_ENTITY_DATE_TIME),
            MasterEventEntity.NOTIFY_DATE_TIME.as(MASTER_EVENT_ENTITY_NOTIFY_DATE_TIME),
            MasterEventEntity.NOTIFY_TIME_IN_MINUTES.as(MASTER_EVENT_ENTITY_NOTIFY_TIME_IN_MINUTES),
            MasterEventEntity.NOTE.as(MASTER_EVENT_ENTITY_NOTE),
            MasterEventEntity.DONE.as(MASTER_EVENT_ENTITY_DONE),
            MasterEventEntity.LINEAR_GROUP.as(MASTER_EVENT_ENTITY_LINEAR_GROUP),
            // child
            ChildEntity.ID.as(CHILD_ENTITY_ID),
            // diaper event
            DiaperEventEntity.ID.as(DIAPER_EVENT_ENTITY_ID),
            DiaperEventEntity.DIAPER_STATE.as(DIAPER_EVENT_ENTITY_DIAPER_STATE),
            // feed event
            FeedEventEntity.ID.as(FEED_EVENT_ENTITY_ID),
            FeedEventEntity.FEED_TYPE.as(FEED_EVENT_ENTITY_FEED_TYPE),
            FeedEventEntity.BREAST.as(FEED_EVENT_ENTITY_BREAST),
            // food
            FoodEntity.NAME_EN.as(FOOD_ENTITY_NAME_EN),
            FoodEntity.NAME_RU.as(FOOD_ENTITY_NAME_RU),
            FoodEntity.NAME_USER.as(FOOD_ENTITY_NAME_USER),
            // other event
            OtherEventEntity.ID.as(OTHER_EVENT_ENTITY_ID),
            OtherEventEntity.NAME.as(OTHER_EVENT_ENTITY_NAME),
            // pump event
            PumpEventEntity.ID.as(PUMP_EVENT_ENTITY_ID),
            PumpEventEntity.BREAST.as(PUMP_EVENT_ENTITY_BREAST),
            // sleep event
            SleepEventEntity.ID.as(SLEEP_EVENT_ENTITY_ID),
            SleepEventEntity.FINISH_DATE_TIME.as(SLEEP_EVENT_ENTITY_FINISH_DATE_TIME),
            SleepEventEntity.TIMER_STARTED.as(SLEEP_EVENT_ENTITY_TIMER_STARTED),
            // doctor visit event
            DoctorVisitEventEntity.ID.as(DOCTOR_VISIT_EVENT_ENTITY_ID),
            DoctorVisitEventEntity.NAME.as(DOCTOR_VISIT_EVENT_ENTITY_NAME),
            DoctorVisitEventEntity.IMAGE_FILE_NAME.as(DOCTOR_VISIT_EVENT_ENTITY_IMAGE_FILE_NAME),
            // doctor visit
            DoctorVisitEntity.ID.as(DOCTOR_VISIT_ENTITY_ID),
            // doctor
            DoctorEntity.NAME_EN.as(DOCTOR_ENTITY_NAME_EN),
            DoctorEntity.NAME_RU.as(DOCTOR_ENTITY_NAME_RU),
            DoctorEntity.NAME_USER.as(DOCTOR_ENTITY_NAME_USER),
            // medicine taking event
            MedicineTakingEventEntity.ID.as(MEDICINE_TAKING_EVENT_ENTITY_ID),
            MedicineTakingEventEntity.IMAGE_FILE_NAME.as(MEDICINE_TAKING_EVENT_ENTITY_IMAGE_FILE_NAME),
            // medicine taking
            MedicineTakingEntity.ID.as(MEDICINE_TAKING_ENTITY_ID),
            // medicine
            MedicineEntity.NAME_EN.as(MEDICINE_ENTITY_NAME_EN),
            MedicineEntity.NAME_RU.as(MEDICINE_ENTITY_NAME_RU),
            MedicineEntity.NAME_USER.as(MEDICINE_ENTITY_NAME_USER),
            // exercise event
            ExerciseEventEntity.ID.as(EXERCISE_EVENT_ENTITY_ID),
            ExerciseEventEntity.NAME.as(EXERCISE_EVENT_ENTITY_NAME),
            ExerciseEventEntity.IMAGE_FILE_NAME.as(EXERCISE_EVENT_ENTITY_IMAGE_FILE_NAME),
            // concrete exercise
            ConcreteExerciseEntity.ID.as(CONCRETE_EXERCISE_ENTITY_ID),
            // exercise
            ExerciseEntity.NAME_EN.as(EXERCISE_ENTITY_NAME_EN),
            ExerciseEntity.NAME_RU.as(EXERCISE_ENTITY_NAME_RU)
    };

    private final ReactiveEntityStore<Persistable> dataStore;

    @Inject
    public AllEventsDbService(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    private static Child mapToChild(@NonNull Tuple data) {
        return Child.builder()
                .id(data.get(ChildEntity.ID.as(CHILD_ENTITY_ID)))
                .build();
    }

    private static DiaperEvent mapToDiaperEvent(@NonNull Tuple data) {
        return DiaperEvent.builder()
                .id(data.get(DiaperEventEntity.ID.as(DIAPER_EVENT_ENTITY_ID)))
                .masterEventId(data.get(MasterEventEntity.ID.as(MASTER_EVENT_ENTITY_ID)))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE.as(MASTER_EVENT_ENTITY_EVENT_TYPE)))
                .dateTime(data.get(MasterEventEntity.DATE_TIME.as(MASTER_EVENT_ENTITY_DATE_TIME)))
                .notifyDateTime(data.get(MasterEventEntity.NOTIFY_DATE_TIME.as(MASTER_EVENT_ENTITY_NOTIFY_DATE_TIME)))
                .notifyTimeInMinutes(data.get(MasterEventEntity.NOTIFY_TIME_IN_MINUTES.as(MASTER_EVENT_ENTITY_NOTIFY_TIME_IN_MINUTES)))
                .note(data.get(MasterEventEntity.NOTE.as(MASTER_EVENT_ENTITY_NOTE)))
                .isDone(data.get(MasterEventEntity.DONE.as(MASTER_EVENT_ENTITY_DONE)))
                .child(mapToChild(data))
                .linearGroup(data.get(MasterEventEntity.LINEAR_GROUP.as(MASTER_EVENT_ENTITY_LINEAR_GROUP)))
                .diaperState(data.get(DiaperEventEntity.DIAPER_STATE.as(DIAPER_EVENT_ENTITY_DIAPER_STATE)))
                .build();
    }

    private static FeedEvent mapToFeedEvent(@NonNull Tuple data) {
        return FeedEvent.builder()
                .id(data.get(FeedEventEntity.ID.as(FEED_EVENT_ENTITY_ID)))
                .masterEventId(data.get(MasterEventEntity.ID.as(MASTER_EVENT_ENTITY_ID)))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE.as(MASTER_EVENT_ENTITY_EVENT_TYPE)))
                .dateTime(data.get(MasterEventEntity.DATE_TIME.as(MASTER_EVENT_ENTITY_DATE_TIME)))
                .notifyDateTime(data.get(MasterEventEntity.NOTIFY_DATE_TIME.as(MASTER_EVENT_ENTITY_NOTIFY_DATE_TIME)))
                .notifyTimeInMinutes(data.get(MasterEventEntity.NOTIFY_TIME_IN_MINUTES.as(MASTER_EVENT_ENTITY_NOTIFY_TIME_IN_MINUTES)))
                .note(data.get(MasterEventEntity.NOTE.as(MASTER_EVENT_ENTITY_NOTE)))
                .isDone(data.get(MasterEventEntity.DONE.as(MASTER_EVENT_ENTITY_DONE)))
                .child(mapToChild(data))
                .linearGroup(data.get(MasterEventEntity.LINEAR_GROUP.as(MASTER_EVENT_ENTITY_LINEAR_GROUP)))
                .feedType(data.get(FeedEventEntity.FEED_TYPE.as(FEED_EVENT_ENTITY_FEED_TYPE)))
                .breast(data.get(FeedEventEntity.BREAST.as(FEED_EVENT_ENTITY_BREAST)))
                .food(mapToFood(data))
                .build();
    }

    private static Food mapToFood(@NonNull Tuple data) {
        return Food.builder()
                .nameEn(data.get(FoodEntity.NAME_EN.as(FOOD_ENTITY_NAME_EN)))
                .nameRu(data.get(FoodEntity.NAME_RU.as(FOOD_ENTITY_NAME_RU)))
                .nameUser(data.get(FoodEntity.NAME_USER.as(FOOD_ENTITY_NAME_USER)))
                .build();
    }

    private static OtherEvent mapToOtherEvent(@NonNull Tuple data) {
        return OtherEvent.builder()
                .id(data.get(OtherEventEntity.ID.as(OTHER_EVENT_ENTITY_ID)))
                .masterEventId(data.get(MasterEventEntity.ID.as(MASTER_EVENT_ENTITY_ID)))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE.as(MASTER_EVENT_ENTITY_EVENT_TYPE)))
                .dateTime(data.get(MasterEventEntity.DATE_TIME.as(MASTER_EVENT_ENTITY_DATE_TIME)))
                .notifyDateTime(data.get(MasterEventEntity.NOTIFY_DATE_TIME.as(MASTER_EVENT_ENTITY_NOTIFY_DATE_TIME)))
                .notifyTimeInMinutes(data.get(MasterEventEntity.NOTIFY_TIME_IN_MINUTES.as(MASTER_EVENT_ENTITY_NOTIFY_TIME_IN_MINUTES)))
                .note(data.get(MasterEventEntity.NOTE.as(MASTER_EVENT_ENTITY_NOTE)))
                .isDone(data.get(MasterEventEntity.DONE.as(MASTER_EVENT_ENTITY_DONE)))
                .child(mapToChild(data))
                .linearGroup(data.get(MasterEventEntity.LINEAR_GROUP.as(MASTER_EVENT_ENTITY_LINEAR_GROUP)))
                .name(data.get(OtherEventEntity.NAME.as(OTHER_EVENT_ENTITY_NAME)))
                .build();
    }

    private static PumpEvent mapToPumpEvent(@NonNull Tuple data) {
        return PumpEvent.builder()
                .id(data.get(PumpEventEntity.ID.as(PUMP_EVENT_ENTITY_ID)))
                .masterEventId(data.get(MasterEventEntity.ID.as(MASTER_EVENT_ENTITY_ID)))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE.as(MASTER_EVENT_ENTITY_EVENT_TYPE)))
                .dateTime(data.get(MasterEventEntity.DATE_TIME.as(MASTER_EVENT_ENTITY_DATE_TIME)))
                .notifyDateTime(data.get(MasterEventEntity.NOTIFY_DATE_TIME.as(MASTER_EVENT_ENTITY_NOTIFY_DATE_TIME)))
                .notifyTimeInMinutes(data.get(MasterEventEntity.NOTIFY_TIME_IN_MINUTES.as(MASTER_EVENT_ENTITY_NOTIFY_TIME_IN_MINUTES)))
                .note(data.get(MasterEventEntity.NOTE.as(MASTER_EVENT_ENTITY_NOTE)))
                .isDone(data.get(MasterEventEntity.DONE.as(MASTER_EVENT_ENTITY_DONE)))
                .child(mapToChild(data))
                .linearGroup(data.get(MasterEventEntity.LINEAR_GROUP.as(MASTER_EVENT_ENTITY_LINEAR_GROUP)))
                .breast(data.get(PumpEventEntity.BREAST.as(PUMP_EVENT_ENTITY_BREAST)))
                .build();
    }

    private static SleepEvent mapToSleepEvent(@NonNull Tuple data) {
        return SleepEvent.builder()
                .id(data.get(SleepEventEntity.ID.as(SLEEP_EVENT_ENTITY_ID)))
                .masterEventId(data.get(MasterEventEntity.ID.as(MASTER_EVENT_ENTITY_ID)))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE.as(MASTER_EVENT_ENTITY_EVENT_TYPE)))
                .dateTime(data.get(MasterEventEntity.DATE_TIME.as(MASTER_EVENT_ENTITY_DATE_TIME)))
                .notifyDateTime(data.get(MasterEventEntity.NOTIFY_DATE_TIME.as(MASTER_EVENT_ENTITY_NOTIFY_DATE_TIME)))
                .notifyTimeInMinutes(data.get(MasterEventEntity.NOTIFY_TIME_IN_MINUTES.as(MASTER_EVENT_ENTITY_NOTIFY_TIME_IN_MINUTES)))
                .note(data.get(MasterEventEntity.NOTE.as(MASTER_EVENT_ENTITY_NOTE)))
                .isDone(data.get(MasterEventEntity.DONE.as(MASTER_EVENT_ENTITY_DONE)))
                .child(mapToChild(data))
                .linearGroup(data.get(MasterEventEntity.LINEAR_GROUP.as(MASTER_EVENT_ENTITY_LINEAR_GROUP)))
                .finishDateTime(data.get(SleepEventEntity.FINISH_DATE_TIME.as(SLEEP_EVENT_ENTITY_FINISH_DATE_TIME)))
                .isTimerStarted(data.get(SleepEventEntity.TIMER_STARTED.as(SLEEP_EVENT_ENTITY_TIMER_STARTED)))
                .build();
    }

    private static DoctorVisitEvent mapToDoctorVisitEvent(@NonNull Tuple data) {
        return DoctorVisitEvent.builder()
                .id(data.get(DoctorVisitEventEntity.ID.as(DOCTOR_VISIT_EVENT_ENTITY_ID)))
                .masterEventId(data.get(MasterEventEntity.ID.as(MASTER_EVENT_ENTITY_ID)))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE.as(MASTER_EVENT_ENTITY_EVENT_TYPE)))
                .dateTime(data.get(MasterEventEntity.DATE_TIME.as(MASTER_EVENT_ENTITY_DATE_TIME)))
                .notifyDateTime(data.get(MasterEventEntity.NOTIFY_DATE_TIME.as(MASTER_EVENT_ENTITY_NOTIFY_DATE_TIME)))
                .notifyTimeInMinutes(data.get(MasterEventEntity.NOTIFY_TIME_IN_MINUTES.as(MASTER_EVENT_ENTITY_NOTIFY_TIME_IN_MINUTES)))
                .note(data.get(MasterEventEntity.NOTE.as(MASTER_EVENT_ENTITY_NOTE)))
                .isDone(data.get(MasterEventEntity.DONE.as(MASTER_EVENT_ENTITY_DONE)))
                .child(mapToChild(data))
                .linearGroup(data.get(MasterEventEntity.LINEAR_GROUP.as(MASTER_EVENT_ENTITY_LINEAR_GROUP)))
                .doctorVisit(mapToDoctorVisit(data))
                .name(data.get(DoctorVisitEventEntity.NAME.as(DOCTOR_VISIT_EVENT_ENTITY_NAME)))
                .doctor(mapToDoctor(data))
                .imageFileName(data.get(DoctorVisitEventEntity.IMAGE_FILE_NAME.as(DOCTOR_VISIT_EVENT_ENTITY_IMAGE_FILE_NAME)))
                .build();
    }

    private static DoctorVisit mapToDoctorVisit(@NonNull Tuple data) {
        return DoctorVisit.builder()
                .id(data.get(DoctorVisitEntity.ID.as(DOCTOR_VISIT_ENTITY_ID)))
                .build();
    }

    private static Doctor mapToDoctor(@NonNull Tuple data) {
        return Doctor.builder()
                .nameEn(data.get(DoctorEntity.NAME_EN.as(DOCTOR_ENTITY_NAME_EN)))
                .nameRu(data.get(DoctorEntity.NAME_RU.as(DOCTOR_ENTITY_NAME_RU)))
                .nameUser(data.get(DoctorEntity.NAME_USER.as(DOCTOR_ENTITY_NAME_USER)))
                .build();
    }

    private static MedicineTakingEvent mapToMedicineTakingEvent(@NonNull Tuple data) {
        return MedicineTakingEvent.builder()
                .id(data.get(MedicineTakingEventEntity.ID.as(MEDICINE_TAKING_EVENT_ENTITY_ID)))
                .masterEventId(data.get(MasterEventEntity.ID.as(MASTER_EVENT_ENTITY_ID)))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE.as(MASTER_EVENT_ENTITY_EVENT_TYPE)))
                .dateTime(data.get(MasterEventEntity.DATE_TIME.as(MASTER_EVENT_ENTITY_DATE_TIME)))
                .notifyDateTime(data.get(MasterEventEntity.NOTIFY_DATE_TIME.as(MASTER_EVENT_ENTITY_NOTIFY_DATE_TIME)))
                .notifyTimeInMinutes(data.get(MasterEventEntity.NOTIFY_TIME_IN_MINUTES.as(MASTER_EVENT_ENTITY_NOTIFY_TIME_IN_MINUTES)))
                .note(data.get(MasterEventEntity.NOTE.as(MASTER_EVENT_ENTITY_NOTE)))
                .isDone(data.get(MasterEventEntity.DONE.as(MASTER_EVENT_ENTITY_DONE)))
                .child(mapToChild(data))
                .linearGroup(data.get(MasterEventEntity.LINEAR_GROUP.as(MASTER_EVENT_ENTITY_LINEAR_GROUP)))
                .medicineTaking(mapToMedicineTaking(data))
                .medicine(mapToMedicine(data))
                .imageFileName(data.get(MedicineTakingEventEntity.IMAGE_FILE_NAME.as(MEDICINE_TAKING_EVENT_ENTITY_IMAGE_FILE_NAME)))
                .build();
    }

    private static MedicineTaking mapToMedicineTaking(@NonNull Tuple data) {
        return MedicineTaking.builder()
                .id(data.get(MedicineTakingEntity.ID.as(MEDICINE_TAKING_ENTITY_ID)))
                .build();
    }

    private static Medicine mapToMedicine(@NonNull Tuple data) {
        return Medicine.builder()
                .nameEn(data.get(MedicineEntity.NAME_EN.as(MEDICINE_ENTITY_NAME_EN)))
                .nameRu(data.get(MedicineEntity.NAME_RU.as(MEDICINE_ENTITY_NAME_RU)))
                .nameUser(data.get(MedicineEntity.NAME_USER.as(MEDICINE_ENTITY_NAME_USER)))
                .build();
    }

    private static ExerciseEvent mapToExerciseEvent(@NonNull Tuple data) {
        return ExerciseEvent.builder()
                .id(data.get(ExerciseEventEntity.ID.as(EXERCISE_EVENT_ENTITY_ID)))
                .masterEventId(data.get(MasterEventEntity.ID.as(MASTER_EVENT_ENTITY_ID)))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE.as(MASTER_EVENT_ENTITY_EVENT_TYPE)))
                .dateTime(data.get(MasterEventEntity.DATE_TIME.as(MASTER_EVENT_ENTITY_DATE_TIME)))
                .notifyDateTime(data.get(MasterEventEntity.NOTIFY_DATE_TIME.as(MASTER_EVENT_ENTITY_NOTIFY_DATE_TIME)))
                .notifyTimeInMinutes(data.get(MasterEventEntity.NOTIFY_TIME_IN_MINUTES.as(MASTER_EVENT_ENTITY_NOTIFY_TIME_IN_MINUTES)))
                .note(data.get(MasterEventEntity.NOTE.as(MASTER_EVENT_ENTITY_NOTE)))
                .isDone(data.get(MasterEventEntity.DONE.as(MASTER_EVENT_ENTITY_DONE)))
                .child(mapToChild(data))
                .linearGroup(data.get(MasterEventEntity.LINEAR_GROUP.as(MASTER_EVENT_ENTITY_LINEAR_GROUP)))
                .concreteExercise(mapToConcreteExercise(data))
                .name(data.get(ExerciseEventEntity.NAME.as(EXERCISE_EVENT_ENTITY_NAME)))
                .imageFileName(data.get(ExerciseEventEntity.IMAGE_FILE_NAME.as(EXERCISE_EVENT_ENTITY_IMAGE_FILE_NAME)))
                .build();
    }

    private static ConcreteExercise mapToConcreteExercise(@NonNull Tuple data) {
        return ConcreteExercise.builder()
                .id(data.get(ConcreteExerciseEntity.ID.as(CONCRETE_EXERCISE_ENTITY_ID)))
                .exercise(mapToExercise(data))
                .build();
    }

    private static Exercise mapToExercise(@NonNull Tuple data) {
        return Exercise.builder()
                .nameEn(data.get(ExerciseEntity.NAME_EN.as(EXERCISE_ENTITY_NAME_EN)))
                .nameRu(data.get(ExerciseEntity.NAME_RU.as(EXERCISE_ENTITY_NAME_RU)))
                .build();
    }

    public Observable<GetEventsResponse> getAllEvents(@NonNull GetEventsRequest request) {
        Child child = request.getChild();
        LocalDate date = request.getDate();
        Set<EventType> eventTypes = request.getFilter().getEventTypes();
        QueryAttribute<MasterEventEntity, DateTime> dateTimeAttribute = request.isGetScheduled()
                ? MasterEventEntity.NOTIFY_DATE_TIME : MasterEventEntity.DATE_TIME;
        WhereAndOr<ReactiveResult<Tuple>> select = dataStore.select(EXPRESSIONS)
                .from(MasterEventEntity.class)
                .join(ChildEntity.class).on(ChildEntity.ID.eq(MasterEventEntity.CHILD_ID))
                .leftJoin(DiaperEventEntity.class).on(DiaperEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .leftJoin(FeedEventEntity.class).on(FeedEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .leftJoin(FoodEntity.class).on(FoodEntity.ID.eq(FeedEventEntity.FOOD_ID))
                .leftJoin(OtherEventEntity.class).on(OtherEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .leftJoin(PumpEventEntity.class).on(PumpEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .leftJoin(SleepEventEntity.class).on(SleepEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .leftJoin(DoctorVisitEventEntity.class).on(DoctorVisitEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .leftJoin(DoctorVisitEntity.class).on(DoctorVisitEntity.ID.eq(DoctorVisitEventEntity.DOCTOR_VISIT_ID))
                .leftJoin(DoctorEntity.class).on(DoctorEntity.ID.eq(DoctorVisitEventEntity.DOCTOR_ID))
                .leftJoin(MedicineTakingEventEntity.class).on(MedicineTakingEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .leftJoin(MedicineTakingEntity.class).on(MedicineTakingEntity.ID.eq(MedicineTakingEventEntity.MEDICINE_TAKING_ID))
                .leftJoin(MedicineEntity.class).on(MedicineEntity.ID.eq(MedicineTakingEventEntity.MEDICINE_ID))
                .leftJoin(ExerciseEventEntity.class).on(ExerciseEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .leftJoin(ConcreteExerciseEntity.class).on(ConcreteExerciseEntity.ID.eq(ExerciseEventEntity.CONCRETE_EXERCISE_ID))
                .leftJoin(ExerciseEntity.class).on(ExerciseEntity.ID.eq(ConcreteExerciseEntity.EXERCISE_ID))
                .where(MasterEventEntity.EVENT_TYPE.notNull())
                .and(MasterEventEntity.EVENT_TYPE.in(eventTypes))
                .and(dateTimeAttribute.greaterThanOrEqual(DateUtils.midnight(date)))
                .and(dateTimeAttribute.lessThan(DateUtils.nextDayMidnight(date)));

        if (child.getId() != null) {
            select = select.and(MasterEventEntity.CHILD_ID.eq(child.getId()));
        }

        return select.orderBy(MasterEventEntity.DATE_TIME, MasterEventEntity.EVENT_TYPE, MasterEventEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, this))
                .map(events -> GetEventsResponse.builder().events(events).request(request).build());
    }

    @Override
    public MasterEvent mapToPlainObject(@NonNull Tuple data) {
        EventType eventType = data.get(MasterEventEntity.EVENT_TYPE.as(MASTER_EVENT_ENTITY_EVENT_TYPE));
        if (eventType == null) {
            throw new IllegalArgumentException("Event type is null");
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
            case EXERCISE:
                return mapToExerciseEvent(data);
        }
        throw new IllegalArgumentException("Unsupported event type");
    }

    @Override
    public Tuple mapToEntity(BlockingEntityStore blockingEntityStore, @NonNull MasterEvent masterEvent) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void fillNonReferencedFields(@NonNull Tuple to, @NonNull MasterEvent from) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
