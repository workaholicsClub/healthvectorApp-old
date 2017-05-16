package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.query.Expression;
import io.requery.query.Tuple;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.entities.calendar.events.DoctorVisitEventEntity;
import ru.android.childdiary.data.entities.calendar.events.MedicineTakingEventEntity;
import ru.android.childdiary.data.entities.calendar.events.core.FoodEntity;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
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
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.events.DoctorVisitEvent;
import ru.android.childdiary.domain.interactors.calendar.events.MedicineTakingEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.Food;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.calendar.requests.GetEventsRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.GetEventsResponse;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;

@Singleton
public class AllEventsDbService implements EntityMapper<Tuple, Tuple, MasterEvent> {
    private static final String MASTER_EVENT_ENTITY_ID = "MASTER_EVENT_ENTITY_ID";
    private static final String MASTER_EVENT_ENTITY_EVENT_TYPE = "MASTER_EVENT_ENTITY_EVENT_TYPE";
    private static final String MASTER_EVENT_ENTITY_DATE_TIME = "MASTER_EVENT_ENTITY_DATE_TIME";
    private static final String MASTER_EVENT_ENTITY_DONE = "MASTER_EVENT_ENTITY_DONE";
    private static final String MASTER_EVENT_ENTITY_LINEAR_GROUP = "MASTER_EVENT_ENTITY_LINEAR_GROUP";
    private static final String CHILD_ENTITY_ID = "CHILD_ENTITY_ID";
    private static final String DIAPER_EVENT_ENTITY_DIAPER_STATE = "DIAPER_EVENT_ENTITY_DIAPER_STATE";
    private static final String FEED_EVENT_ENTITY_FEED_TYPE = "FEED_EVENT_ENTITY_FEED_TYPE";
    private static final String FEED_EVENT_ENTITY_BREAST = "FEED_EVENT_ENTITY_BREAST";
    private static final String FOOD_ENTITY_NAME = "FOOD_ENTITY_NAME";
    private static final String OTHER_EVENT_ENTITY_NAME = "OTHER_EVENT_ENTITY_NAME";
    private static final String PUMP_EVENT_ENTITY_BREAST = "PUMP_EVENT_ENTITY_BREAST";
    private static final String SLEEP_EVENT_ENTITY_FINISH_DATE_TIME = "SLEEP_EVENT_ENTITY_FINISH_DATE_TIME";
    private static final String SLEEP_EVENT_ENTITY_TIMER_STARTED = "SLEEP_EVENT_ENTITY_TIMER_STARTED";
    private static final String DOCTOR_VISIT_EVENT_ENTITY_NAME = "DOCTOR_VISIT_EVENT_ENTITY_NAME";
    private static final String DOCTOR_VISIT_ENTITY_ID = "DOCTOR_VISIT_ENTITY_ID";
    private static final String DOCTOR_ENTITY_NAME = "DOCTOR_ENTITY_NAME";
    private static final String MEDICINE_TAKING_ENTITY_ID = "MEDICINE_TAKING_ENTITY_ID";
    private static final String MEDICINE_ENTITY_NAME = "MEDICINE_ENTITY_NAME";

    private final static Expression[] EXPRESSIONS = new Expression[]{
            MasterEventEntity.ID.as(MASTER_EVENT_ENTITY_ID),
            MasterEventEntity.EVENT_TYPE.as(MASTER_EVENT_ENTITY_EVENT_TYPE),
            MasterEventEntity.DATE_TIME.as(MASTER_EVENT_ENTITY_DATE_TIME),
            MasterEventEntity.DONE.as(MASTER_EVENT_ENTITY_DONE),
            MasterEventEntity.LINEAR_GROUP.as(MASTER_EVENT_ENTITY_LINEAR_GROUP),
            // child
            ChildEntity.ID.as(CHILD_ENTITY_ID),
            // diaper event
            DiaperEventEntity.DIAPER_STATE.as(DIAPER_EVENT_ENTITY_DIAPER_STATE),
            // feed event
            FeedEventEntity.FEED_TYPE.as(FEED_EVENT_ENTITY_FEED_TYPE),
            FeedEventEntity.BREAST.as(FEED_EVENT_ENTITY_BREAST),
            // food
            FoodEntity.NAME.as(FOOD_ENTITY_NAME),
            // other event
            OtherEventEntity.NAME.as(OTHER_EVENT_ENTITY_NAME),
            // pump event
            PumpEventEntity.BREAST.as(PUMP_EVENT_ENTITY_BREAST),
            // sleep event
            SleepEventEntity.FINISH_DATE_TIME.as(SLEEP_EVENT_ENTITY_FINISH_DATE_TIME),
            SleepEventEntity.TIMER_STARTED.as(SLEEP_EVENT_ENTITY_TIMER_STARTED),
            // doctor visit event
            DoctorVisitEventEntity.NAME.as(DOCTOR_VISIT_EVENT_ENTITY_NAME),
            // doctor visit
            DoctorVisitEntity.ID.as(DOCTOR_VISIT_ENTITY_ID),
            // doctor
            DoctorEntity.NAME.as(DOCTOR_ENTITY_NAME),
            // medicine taking event
            // medicine taking
            MedicineTakingEntity.ID.as(MEDICINE_TAKING_ENTITY_ID),
            // medicine
            MedicineEntity.NAME.as(MEDICINE_ENTITY_NAME)
    };

    private final ReactiveEntityStore<Persistable> dataStore;

    @Inject
    public AllEventsDbService(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    private static DateTime midnight(LocalDate date) {
        return date.toDateTime(LocalTime.MIDNIGHT);
    }

    private static DateTime nextDayMidnight(LocalDate date) {
        return date.plusDays(1).toDateTime(LocalTime.MIDNIGHT);
    }

    private static Child mapToChild(@NonNull Tuple data) {
        return Child.builder()
                .id(data.get(ChildEntity.ID.as(CHILD_ENTITY_ID)))
                .build();
    }

    private static DiaperEvent mapToDiaperEvent(@NonNull Tuple data) {
        return DiaperEvent.builder()
                .masterEventId(data.get(MasterEventEntity.ID.as(MASTER_EVENT_ENTITY_ID)))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE.as(MASTER_EVENT_ENTITY_EVENT_TYPE)))
                .dateTime(data.get(MasterEventEntity.DATE_TIME.as(MASTER_EVENT_ENTITY_DATE_TIME)))
                .isDone(data.get(MasterEventEntity.DONE.as(MASTER_EVENT_ENTITY_DONE)))
                .child(mapToChild(data))
                .linearGroup(data.get(MasterEventEntity.LINEAR_GROUP.as(MASTER_EVENT_ENTITY_LINEAR_GROUP)))
                .diaperState(data.get(DiaperEventEntity.DIAPER_STATE.as(DIAPER_EVENT_ENTITY_DIAPER_STATE)))
                .build();
    }

    private static FeedEvent mapToFeedEvent(@NonNull Tuple data) {
        return FeedEvent.builder()
                .masterEventId(data.get(MasterEventEntity.ID.as(MASTER_EVENT_ENTITY_ID)))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE.as(MASTER_EVENT_ENTITY_EVENT_TYPE)))
                .dateTime(data.get(MasterEventEntity.DATE_TIME.as(MASTER_EVENT_ENTITY_DATE_TIME)))
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
                .name(data.get(FoodEntity.NAME.as(FOOD_ENTITY_NAME)))
                .build();
    }

    private static OtherEvent mapToOtherEvent(@NonNull Tuple data) {
        return OtherEvent.builder()
                .masterEventId(data.get(MasterEventEntity.ID.as(MASTER_EVENT_ENTITY_ID)))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE.as(MASTER_EVENT_ENTITY_EVENT_TYPE)))
                .dateTime(data.get(MasterEventEntity.DATE_TIME.as(MASTER_EVENT_ENTITY_DATE_TIME)))
                .isDone(data.get(MasterEventEntity.DONE.as(MASTER_EVENT_ENTITY_DONE)))
                .child(mapToChild(data))
                .linearGroup(data.get(MasterEventEntity.LINEAR_GROUP.as(MASTER_EVENT_ENTITY_LINEAR_GROUP)))
                .name(data.get(OtherEventEntity.NAME.as(OTHER_EVENT_ENTITY_NAME)))
                .build();
    }

    private static PumpEvent mapToPumpEvent(@NonNull Tuple data) {
        return PumpEvent.builder()
                .masterEventId(data.get(MasterEventEntity.ID.as(MASTER_EVENT_ENTITY_ID)))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE.as(MASTER_EVENT_ENTITY_EVENT_TYPE)))
                .dateTime(data.get(MasterEventEntity.DATE_TIME.as(MASTER_EVENT_ENTITY_DATE_TIME)))
                .isDone(data.get(MasterEventEntity.DONE.as(MASTER_EVENT_ENTITY_DONE)))
                .child(mapToChild(data))
                .linearGroup(data.get(MasterEventEntity.LINEAR_GROUP.as(MASTER_EVENT_ENTITY_LINEAR_GROUP)))
                .breast(data.get(PumpEventEntity.BREAST.as(PUMP_EVENT_ENTITY_BREAST)))
                .build();
    }

    private static SleepEvent mapToSleepEvent(@NonNull Tuple data) {
        return SleepEvent.builder()
                .masterEventId(data.get(MasterEventEntity.ID.as(MASTER_EVENT_ENTITY_ID)))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE.as(MASTER_EVENT_ENTITY_EVENT_TYPE)))
                .dateTime(data.get(MasterEventEntity.DATE_TIME.as(MASTER_EVENT_ENTITY_DATE_TIME)))
                .isDone(data.get(MasterEventEntity.DONE.as(MASTER_EVENT_ENTITY_DONE)))
                .child(mapToChild(data))
                .linearGroup(data.get(MasterEventEntity.LINEAR_GROUP.as(MASTER_EVENT_ENTITY_LINEAR_GROUP)))
                .finishDateTime(data.get(SleepEventEntity.FINISH_DATE_TIME.as(SLEEP_EVENT_ENTITY_FINISH_DATE_TIME)))
                .isTimerStarted(data.get(SleepEventEntity.TIMER_STARTED.as(SLEEP_EVENT_ENTITY_TIMER_STARTED)))
                .build();
    }

    private static DoctorVisitEvent mapToDoctorVisitEvent(@NonNull Tuple data) {
        return DoctorVisitEvent.builder()
                .masterEventId(data.get(MasterEventEntity.ID.as(MASTER_EVENT_ENTITY_ID)))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE.as(MASTER_EVENT_ENTITY_EVENT_TYPE)))
                .dateTime(data.get(MasterEventEntity.DATE_TIME.as(MASTER_EVENT_ENTITY_DATE_TIME)))
                .isDone(data.get(MasterEventEntity.DONE.as(MASTER_EVENT_ENTITY_DONE)))
                .child(mapToChild(data))
                .linearGroup(data.get(MasterEventEntity.LINEAR_GROUP.as(MASTER_EVENT_ENTITY_LINEAR_GROUP)))
                .doctorVisit(mapToDoctorVisit(data))
                .name(data.get(DoctorVisitEventEntity.NAME.as(DOCTOR_VISIT_EVENT_ENTITY_NAME)))
                .doctor(mapToDoctor(data))
                .build();
    }

    private static DoctorVisit mapToDoctorVisit(@NonNull Tuple data) {
        return DoctorVisit.builder()
                .id(data.get(DoctorVisitEntity.ID.as(DOCTOR_VISIT_ENTITY_ID)))
                .build();
    }

    private static Doctor mapToDoctor(@NonNull Tuple data) {
        return Doctor.builder()
                .name(data.get(DoctorEntity.NAME.as(DOCTOR_ENTITY_NAME)))
                .build();
    }

    private static MedicineTakingEvent mapToMedicineTakingEvent(@NonNull Tuple data) {
        return MedicineTakingEvent.builder()
                .masterEventId(data.get(MasterEventEntity.ID.as(MASTER_EVENT_ENTITY_ID)))
                .eventType(data.get(MasterEventEntity.EVENT_TYPE.as(MASTER_EVENT_ENTITY_EVENT_TYPE)))
                .dateTime(data.get(MasterEventEntity.DATE_TIME.as(MASTER_EVENT_ENTITY_DATE_TIME)))
                .isDone(data.get(MasterEventEntity.DONE.as(MASTER_EVENT_ENTITY_DONE)))
                .child(mapToChild(data))
                .linearGroup(data.get(MasterEventEntity.LINEAR_GROUP.as(MASTER_EVENT_ENTITY_LINEAR_GROUP)))
                .medicineTaking(mapToMedicineTaking(data))
                .medicine(mapToMedicine(data))
                .build();
    }

    private static MedicineTaking mapToMedicineTaking(@NonNull Tuple data) {
        return MedicineTaking.builder()
                .id(data.get(MedicineTakingEntity.ID.as(MEDICINE_TAKING_ENTITY_ID)))
                .build();
    }

    private static Medicine mapToMedicine(@NonNull Tuple data) {
        return Medicine.builder()
                .name(data.get(MedicineEntity.NAME.as(MEDICINE_ENTITY_NAME)))
                .build();
    }

    public Observable<GetEventsResponse> getAllEvents(@NonNull GetEventsRequest request) {
        Child child = request.getChild();
        LocalDate selectedDate = request.getDate();
        return dataStore.select(EXPRESSIONS)
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
                .where(MasterEventEntity.CHILD_ID.eq(child.getId()))
                .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(midnight(selectedDate)))
                .and(MasterEventEntity.DATE_TIME.lessThan(nextDayMidnight(selectedDate)))
                .and(MasterEventEntity.EVENT_TYPE.notNull())
                .orderBy(MasterEventEntity.DATE_TIME, MasterEventEntity.EVENT_TYPE, MasterEventEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, this))
                .map(events -> GetEventsResponse.builder().events(events).request(request).build());
    }

    @Override
    public MasterEvent mapToPlainObject(@NonNull Tuple data) {
        EventType eventType = data.get(MasterEventEntity.EVENT_TYPE.as(MASTER_EVENT_ENTITY_EVENT_TYPE));
        if (eventType == null) {
            throw new IllegalStateException("Invalid event type");
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
            // TODO EXERCISE
        }
        throw new IllegalStateException("Unsupported event type");
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