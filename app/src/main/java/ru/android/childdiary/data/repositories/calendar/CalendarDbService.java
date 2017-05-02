package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.entities.calendar.events.DoctorVisitEventEntity;
import ru.android.childdiary.data.entities.calendar.events.MedicineTakingEventEntity;
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
import ru.android.childdiary.data.entities.medical.core.MedicineEntity;
import ru.android.childdiary.data.repositories.calendar.mappers.AllEventsMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.DiaperEventMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.DoctorVisitEventMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.FeedEventMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.FoodMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.FoodMeasureMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.MasterEventMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.MedicineTakingEventMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.OtherEventMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.PumpEventMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.SleepEventMapper;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.events.DoctorVisitEvent;
import ru.android.childdiary.domain.interactors.calendar.events.MedicineTakingEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.Food;
import ru.android.childdiary.domain.interactors.calendar.events.core.FoodMeasure;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.utils.EventHelper;

@Singleton
public class CalendarDbService {
    private final ReactiveEntityStore<Persistable> dataStore;

    @Inject
    public CalendarDbService(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    private static DateTime midnight(LocalDate date) {
        return date.toDateTime(LocalTime.MIDNIGHT);
    }

    private static DateTime nextDayMidnight(LocalDate date) {
        return date.plusDays(1).toDateTime(LocalTime.MIDNIGHT);
    }

    public Observable<List<FoodMeasure>> getFoodMeasureList() {
        return dataStore.select(FoodMeasureEntity.class)
                .orderBy(FoodMeasureEntity.NAME, FoodMeasureEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, FoodMeasureMapper::mapToPlainObject));
    }

    public Observable<FoodMeasure> addFoodMeasure(@NonNull FoodMeasure foodMeasure) {
        return DbUtils.insertObservable(dataStore, foodMeasure,
                FoodMeasureMapper::mapToEntity, FoodMeasureMapper::mapToPlainObject);
    }

    public Observable<List<Food>> getFoodList() {
        return dataStore.select(FoodEntity.class)
                .orderBy(FoodEntity.NAME, FoodEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, FoodMapper::mapToPlainObject));
    }

    public Observable<Food> addFood(@NonNull Food food) {
        return DbUtils.insertObservable(dataStore, food,
                FoodMapper::mapToEntity, FoodMapper::mapToPlainObject);
    }

    public Observable<List<MasterEvent>> getAllWithoutDetails(@NonNull Child child, @NonNull LocalDate selectedDate) {
        return dataStore.select(MasterEventEntity.class)
                .where(MasterEventEntity.CHILD_ID.eq(child.getId()))
                .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(midnight(selectedDate)))
                .and(MasterEventEntity.DATE_TIME.lessThan(nextDayMidnight(selectedDate)))
                .and(MasterEventEntity.EVENT_TYPE.notNull())
                .orderBy(MasterEventEntity.DATE_TIME, MasterEventEntity.EVENT_TYPE, MasterEventEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, MasterEventMapper::mapToPlainObject));
    }

    public Observable<List<MasterEvent>> getAll(@NonNull Child child, @NonNull LocalDate selectedDate) {
        return dataStore.select(
                // master event
                MasterEventEntity.ID,
                MasterEventEntity.EVENT_TYPE,
                MasterEventEntity.DATE_TIME,
                MasterEventEntity.NOTIFY_TIME_IN_MINUTES,
                MasterEventEntity.NOTE,
                MasterEventEntity.DONE,
                MasterEventEntity.LINEAR_GROUP,
                // repeat parameters
                RepeatParametersEntity.ID.as("repeat_parameters_id"),
                RepeatParametersEntity.DATE_TIME_FROM.as("repeat_parameters_date_time_from"),
                RepeatParametersEntity.PERIODICITY_IN_MINUTES,
                RepeatParametersEntity.LENGTH_IN_MINUTES,
                RepeatParametersEntity.LINEAR_GROUPS,
                // child
                ChildEntity.ID.as("child_id"),
                ChildEntity.NAME.as("child_name"),
                ChildEntity.BIRTH_DATE,
                ChildEntity.BIRTH_TIME,
                ChildEntity.SEX,
                ChildEntity.IMAGE_FILE_NAME,
                ChildEntity.BIRTH_HEIGHT,
                ChildEntity.BIRTH_WEIGHT,
                // diaper event
                DiaperEventEntity.ID.as("diaper_event_id"),
                DiaperEventEntity.DIAPER_STATE,
                // feed event
                FeedEventEntity.ID.as("feed_event_id"),
                FeedEventEntity.FEED_TYPE,
                FeedEventEntity.BREAST.as("feed_event_breast"),
                FeedEventEntity.LEFT_DURATION_IN_MINUTES,
                FeedEventEntity.RIGHT_DURATION_IN_MINUTES,
                FeedEventEntity.AMOUNT,
                FeedEventEntity.AMOUNT_ML,
                // food measure
                FoodMeasureEntity.ID.as("food_measure_id"),
                FoodMeasureEntity.NAME.as("food_measure_name"),
                // food
                FoodEntity.ID.as("food_id"),
                FoodEntity.NAME.as("food_name"),
                // other event
                OtherEventEntity.ID.as("other_event_id"),
                OtherEventEntity.NAME.as("other_event_name"),
                OtherEventEntity.FINISH_DATE_TIME.as("other_event_finish_date_time"),
                // pump event
                PumpEventEntity.ID.as("pump_event_id"),
                PumpEventEntity.BREAST.as("pump_event_breast"),
                PumpEventEntity.LEFT_AMOUNT_ML,
                PumpEventEntity.RIGHT_AMOUNT_ML,
                // sleep event
                SleepEventEntity.ID.as("sleep_event_id"),
                SleepEventEntity.FINISH_DATE_TIME.as("sleep_event_finish_date_time"),
                SleepEventEntity.TIMER_STARTED,
                // doctor visit event
                DoctorVisitEventEntity.ID.as("doctor_visit_event_id"),
                // doctor visit
                DoctorVisitEntity.ID.as("doctor_visit_id"),
                DoctorVisitEntity.NAME.as("doctor_visit_name"),
                DoctorVisitEntity.DURATION_IN_MINUTES.as("doctor_visit_duration_in_minutes"),
                DoctorVisitEntity.DATE_TIME.as("doctor_visit_date_time"),
                DoctorVisitEntity.NOTIFY_TIME_IN_MINUTES.as("doctor_visit_notify_time_in_minutes"),
                DoctorVisitEntity.NOTE.as("doctor_visit_note"),
                DoctorVisitEntity.IMAGE_FILE_NAME.as("doctor_visit_image_file_name"),
                // doctor
                DoctorEntity.ID.as("doctor_id"),
                DoctorEntity.NAME.as("doctor_name"),
                // medicine taking event
                MedicineTakingEventEntity.ID.as("medicine_taking_event_id"),
                // medicine taking
                MedicineTakingEntity.ID.as("medicine_taking_id"),
                MedicineTakingEntity.DATE_TIME.as("medicine_taking_date_time"),
                MedicineTakingEntity.NOTIFY_TIME_IN_MINUTES.as("medicine_taking_notify_time_in_minutes"),
                MedicineTakingEntity.NOTE.as("medicine_taking_note"),
                MedicineTakingEntity.IMAGE_FILE_NAME.as("medicine_taking_image_file_name"),
                // medicine
                MedicineEntity.ID.as("medicine_id"),
                MedicineEntity.NAME.as("medicine_name")
        )
                .from(MasterEventEntity.class)
                .join(ChildEntity.class).on(ChildEntity.ID.eq(MasterEventEntity.CHILD_ID))
                .leftJoin(RepeatParametersEntity.class).on(RepeatParametersEntity.ID.eq(MasterEventEntity.REPEAT_PARAMETERS_ID))
                .leftJoin(DiaperEventEntity.class).on(DiaperEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .leftJoin(FeedEventEntity.class).on(FeedEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .leftJoin(FoodMeasureEntity.class).on(FoodMeasureEntity.ID.eq(FeedEventEntity.FOOD_MEASURE_ID))
                .leftJoin(FoodEntity.class).on(FoodEntity.ID.eq(FeedEventEntity.FOOD_ID))
                .leftJoin(OtherEventEntity.class).on(OtherEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .leftJoin(PumpEventEntity.class).on(PumpEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .leftJoin(SleepEventEntity.class).on(SleepEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .leftJoin(DoctorVisitEventEntity.class).on(DoctorVisitEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .leftJoin(DoctorVisitEntity.class).on(DoctorVisitEntity.ID.eq(DoctorVisitEventEntity.DOCTOR_VISIT_ID))
                .leftJoin(DoctorEntity.class).on(DoctorEntity.ID.eq(DoctorVisitEntity.DOCTOR_ID))
                .leftJoin(MedicineTakingEventEntity.class).on(MedicineTakingEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .leftJoin(MedicineTakingEntity.class).on(MedicineTakingEntity.ID.eq(MedicineTakingEventEntity.MEDICINE_TAKING_ID))
                .leftJoin(MedicineEntity.class).on(MedicineEntity.ID.eq(MedicineTakingEntity.MEDICINE_ID))
                .where(MasterEventEntity.CHILD_ID.eq(child.getId()))
                .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(midnight(selectedDate)))
                .and(MasterEventEntity.DATE_TIME.lessThan(nextDayMidnight(selectedDate)))
                .and(MasterEventEntity.EVENT_TYPE.notNull())
                .orderBy(MasterEventEntity.DATE_TIME, MasterEventEntity.EVENT_TYPE, MasterEventEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, AllEventsMapper::mapToPlainObject));
    }

    public Observable<List<SleepEvent>> getSleepEventsWithTimer() {
        return dataStore.select(SleepEventEntity.class)
                .join(MasterEventEntity.class).on(SleepEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .and(MasterEventEntity.EVENT_TYPE.eq(EventType.SLEEP))
                .and(SleepEventEntity.TIMER_STARTED.eq(true))
                .orderBy(MasterEventEntity.DATE_TIME, MasterEventEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, SleepEventMapper::mapToPlainObject));
    }

    public Observable<DiaperEvent> getDiaperEventDetail(@NonNull MasterEvent event) {
        return dataStore.select(DiaperEventEntity.class)
                .where(DiaperEventEntity.MASTER_EVENT_ID.eq(event.getMasterEventId()))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToObservable(reactiveResult, DiaperEventMapper::mapToPlainObject));
    }

    public Observable<FeedEvent> getFeedEventDetail(@NonNull MasterEvent event) {
        return dataStore.select(FeedEventEntity.class)
                .where(FeedEventEntity.MASTER_EVENT_ID.eq(event.getMasterEventId()))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToObservable(reactiveResult, FeedEventMapper::mapToPlainObject));
    }

    public Observable<OtherEvent> getOtherEventDetail(@NonNull MasterEvent event) {
        return dataStore.select(OtherEventEntity.class)
                .where(OtherEventEntity.MASTER_EVENT_ID.eq(event.getMasterEventId()))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToObservable(reactiveResult, OtherEventMapper::mapToPlainObject));
    }

    public Observable<PumpEvent> getPumpEventDetail(@NonNull MasterEvent event) {
        return dataStore.select(PumpEventEntity.class)
                .where(PumpEventEntity.MASTER_EVENT_ID.eq(event.getMasterEventId()))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToObservable(reactiveResult, PumpEventMapper::mapToPlainObject));
    }

    public Observable<SleepEvent> getSleepEventDetail(@NonNull MasterEvent event) {
        return dataStore.select(SleepEventEntity.class)
                .where(SleepEventEntity.MASTER_EVENT_ID.eq(event.getMasterEventId()))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToObservable(reactiveResult, SleepEventMapper::mapToPlainObject));
    }

    public Observable<DoctorVisitEvent> getDoctorVisitEventDetail(@NonNull MasterEvent event) {
        return dataStore.select(DoctorVisitEventEntity.class)
                .where(DoctorVisitEventEntity.MASTER_EVENT_ID.eq(event.getMasterEventId()))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToObservable(reactiveResult, DoctorVisitEventMapper::mapToPlainObject));
    }

    public Observable<MedicineTakingEvent> getMedicineTakingEventDetail(@NonNull MasterEvent event) {
        return dataStore.select(MedicineTakingEventEntity.class)
                .where(MedicineTakingEventEntity.MASTER_EVENT_ID.eq(event.getMasterEventId()))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToObservable(reactiveResult, MedicineTakingEventMapper::mapToPlainObject));
    }

    public Observable<DiaperEvent> add(@NonNull DiaperEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            MasterEvent masterEvent = insertMasterEvent(event);
            return insertDiaperEvent(event, masterEvent);
        }));
    }

    public Observable<FeedEvent> add(@NonNull FeedEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            MasterEvent masterEvent = insertMasterEvent(event);
            return insertFeedEvent(event, masterEvent);
        }));
    }

    public Observable<OtherEvent> add(@NonNull OtherEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            MasterEvent masterEvent = insertMasterEvent(event);
            return insertOtherEvent(event, masterEvent);
        }));
    }

    public Observable<PumpEvent> add(@NonNull PumpEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            MasterEvent masterEvent = insertMasterEvent(event);
            return insertPumpEvent(event, masterEvent);
        }));
    }

    public Observable<SleepEvent> add(@NonNull SleepEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            MasterEvent masterEvent = insertMasterEvent(event);
            return insertSleepEvent(event, masterEvent);
        }));
    }

    public Observable<DoctorVisitEvent> add(@NonNull DoctorVisitEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            MasterEvent masterEvent = insertMasterEvent(event);
            return insertDoctorVisitEvent(event, masterEvent);
        }));
    }

    public Observable<MedicineTakingEvent> add(@NonNull MedicineTakingEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            MasterEvent masterEvent = insertMasterEvent(event);
            return insertMedicineTakingEvent(event, masterEvent);
        }));
    }

    public Observable<DiaperEvent> update(@NonNull DiaperEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            updateMasterEvent(event);
            return updateDiaperEvent(event);
        }));
    }

    public Observable<FeedEvent> update(@NonNull FeedEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            updateMasterEvent(event);
            return updateFeedEvent(event);
        }));
    }

    public Observable<OtherEvent> update(@NonNull OtherEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            updateMasterEvent(event);
            return updateOtherEvent(event);
        }));
    }

    public Observable<PumpEvent> update(@NonNull PumpEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            updateMasterEvent(event);
            return updatePumpEvent(event);
        }));
    }

    public Observable<SleepEvent> update(@NonNull SleepEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            updateMasterEvent(event);
            return updateSleepEvent(event);
        }));
    }

    public Observable<DoctorVisitEvent> update(@NonNull DoctorVisitEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            updateMasterEvent(event);
            return updateDoctorVisitEvent(event);
        }));
    }

    public Observable<MedicineTakingEvent> update(@NonNull MedicineTakingEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            updateMasterEvent(event);
            return updateMedicineTakingEvent(event);
        }));
    }

    public Observable<MasterEvent> delete(@NonNull MasterEvent event) {
        return DbUtils.deleteObservable(dataStore, MasterEventEntity.class, event, event.getMasterEventId());
    }

    public Observable<MasterEvent> done(@NonNull MasterEvent event) {
        MasterEvent masterEvent = event.toMasterBuilder().isDone(!EventHelper.isDone(event)).build();
        return Observable.fromCallable(() -> updateMasterEvent(masterEvent));
    }

    private MasterEvent insertMasterEvent(@NonNull MasterEvent event) {
        return DbUtils.insert(dataStore, event,
                MasterEventMapper::mapToEntity, MasterEventMapper::mapToPlainObject);
    }

    private MasterEvent updateMasterEvent(@NonNull MasterEvent event) {
        return DbUtils.update(dataStore, event,
                MasterEventMapper::mapToEntity, MasterEventMapper::mapToPlainObject);
    }

    private DiaperEvent insertDiaperEvent(@NonNull DiaperEvent event, @NonNull MasterEvent masterEvent) {
        return DbUtils.insert(dataStore, event, masterEvent,
                DiaperEventMapper::mapToEntity, DiaperEventMapper::mapToPlainObject);
    }

    private FeedEvent insertFeedEvent(@NonNull FeedEvent event, @NonNull MasterEvent masterEvent) {
        return DbUtils.insert(dataStore, event, masterEvent,
                FeedEventMapper::mapToEntity, FeedEventMapper::mapToPlainObject);
    }

    private OtherEvent insertOtherEvent(@NonNull OtherEvent event, @NonNull MasterEvent masterEvent) {
        return DbUtils.insert(dataStore, event, masterEvent,
                OtherEventMapper::mapToEntity, OtherEventMapper::mapToPlainObject);
    }

    private PumpEvent insertPumpEvent(@NonNull PumpEvent event, @NonNull MasterEvent masterEvent) {
        return DbUtils.insert(dataStore, event, masterEvent,
                PumpEventMapper::mapToEntity, PumpEventMapper::mapToPlainObject);
    }

    private SleepEvent insertSleepEvent(@NonNull SleepEvent event, @NonNull MasterEvent masterEvent) {
        return DbUtils.insert(dataStore, event, masterEvent,
                SleepEventMapper::mapToEntity, SleepEventMapper::mapToPlainObject);
    }

    private DoctorVisitEvent insertDoctorVisitEvent(@NonNull DoctorVisitEvent event, @NonNull MasterEvent masterEvent) {
        return DbUtils.insert(dataStore, event, masterEvent,
                DoctorVisitEventMapper::mapToEntity, DoctorVisitEventMapper::mapToPlainObject);
    }

    private MedicineTakingEvent insertMedicineTakingEvent(@NonNull MedicineTakingEvent event, @NonNull MasterEvent masterEvent) {
        return DbUtils.insert(dataStore, event, masterEvent,
                MedicineTakingEventMapper::mapToEntity, MedicineTakingEventMapper::mapToPlainObject);
    }

    private DiaperEvent updateDiaperEvent(@NonNull DiaperEvent event) {
        return DbUtils.update(dataStore, event,
                DiaperEventMapper::mapToEntity, DiaperEventMapper::mapToPlainObject);
    }

    private FeedEvent updateFeedEvent(@NonNull FeedEvent event) {
        return DbUtils.update(dataStore, event,
                FeedEventMapper::mapToEntity, FeedEventMapper::mapToPlainObject);
    }

    private OtherEvent updateOtherEvent(@NonNull OtherEvent event) {
        return DbUtils.update(dataStore, event,
                OtherEventMapper::mapToEntity, OtherEventMapper::mapToPlainObject);
    }

    private PumpEvent updatePumpEvent(@NonNull PumpEvent event) {
        return DbUtils.update(dataStore, event,
                PumpEventMapper::mapToEntity, PumpEventMapper::mapToPlainObject);
    }

    private SleepEvent updateSleepEvent(@NonNull SleepEvent event) {
        return DbUtils.update(dataStore, event,
                SleepEventMapper::mapToEntity, SleepEventMapper::mapToPlainObject);
    }

    private DoctorVisitEvent updateDoctorVisitEvent(@NonNull DoctorVisitEvent event) {
        return DbUtils.update(dataStore, event,
                DoctorVisitEventMapper::mapToEntity, DoctorVisitEventMapper::mapToPlainObject);
    }

    private MedicineTakingEvent updateMedicineTakingEvent(@NonNull MedicineTakingEvent event) {
        return DbUtils.update(dataStore, event,
                MedicineTakingEventMapper::mapToEntity, MedicineTakingEventMapper::mapToPlainObject);
    }
}
