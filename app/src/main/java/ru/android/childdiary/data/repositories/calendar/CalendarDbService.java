package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.query.JoinAndOr;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveResult;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.entities.calendar.events.DoctorVisitEventEntity;
import ru.android.childdiary.data.entities.calendar.events.ExerciseEventEntity;
import ru.android.childdiary.data.entities.calendar.events.MedicineTakingEventEntity;
import ru.android.childdiary.data.entities.calendar.events.core.FoodEntity;
import ru.android.childdiary.data.entities.calendar.events.core.FoodMeasureEntity;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.DiaperEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.FeedEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.OtherEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.PumpEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.SleepEventEntity;
import ru.android.childdiary.data.entities.medical.core.DoctorEntity;
import ru.android.childdiary.data.entities.medical.core.MedicineEntity;
import ru.android.childdiary.data.entities.medical.core.MedicineMeasureEntity;
import ru.android.childdiary.data.repositories.calendar.mappers.DiaperEventMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.DoctorVisitEventMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.ExerciseEventMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.FeedEventMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.FoodMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.FoodMeasureMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.MasterEventMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.MedicineTakingEventMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.OtherEventMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.PumpEventMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.SleepEventMapper;
import ru.android.childdiary.data.repositories.core.EventsDbService;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.events.DoctorVisitEvent;
import ru.android.childdiary.domain.interactors.calendar.events.ExerciseEvent;
import ru.android.childdiary.domain.interactors.calendar.events.MedicineTakingEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.Food;
import ru.android.childdiary.domain.interactors.calendar.events.core.FoodMeasure;
import ru.android.childdiary.domain.interactors.calendar.events.core.LinearGroupFieldType;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.calendar.requests.GetDoctorVisitEventsRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.GetDoctorVisitEventsResponse;
import ru.android.childdiary.domain.interactors.calendar.requests.GetMedicineTakingEventsRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.GetMedicineTakingEventsResponse;
import ru.android.childdiary.domain.interactors.calendar.requests.GetSleepEventsRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.GetSleepEventsResponse;
import ru.android.childdiary.domain.interactors.calendar.requests.UpdateDoctorVisitEventRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.UpdateDoctorVisitEventResponse;
import ru.android.childdiary.domain.interactors.calendar.requests.UpdateExerciseEventRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.UpdateExerciseEventResponse;
import ru.android.childdiary.domain.interactors.calendar.requests.UpdateMedicineTakingEventRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.UpdateMedicineTakingEventResponse;

@Singleton
public class CalendarDbService extends EventsDbService {
    private final DiaperEventMapper diaperEventMapper;
    private final DoctorVisitEventMapper doctorVisitEventMapper;
    private final ExerciseEventMapper exerciseEventMapper;
    private final FeedEventMapper feedEventMapper;
    private final FoodMapper foodMapper;
    private final FoodMeasureMapper foodMeasureMapper;
    private final MasterEventMapper masterEventMapper;
    private final MedicineTakingEventMapper medicineTakingEventMapper;
    private final OtherEventMapper otherEventMapper;
    private final PumpEventMapper pumpEventMapper;
    private final SleepEventMapper sleepEventMapper;

    @Inject
    public CalendarDbService(ReactiveEntityStore<Persistable> dataStore,
                             DiaperEventMapper diaperEventMapper,
                             DoctorVisitEventMapper doctorVisitEventMapper,
                             FeedEventMapper feedEventMapper,
                             FoodMapper foodMapper,
                             FoodMeasureMapper foodMeasureMapper,
                             ExerciseEventMapper exerciseEventMapper,
                             MasterEventMapper masterEventMapper,
                             MedicineTakingEventMapper medicineTakingEventMapper,
                             OtherEventMapper otherEventMapper,
                             PumpEventMapper pumpEventMapper,
                             SleepEventMapper sleepEventMapper) {
        super(dataStore);
        this.diaperEventMapper = diaperEventMapper;
        this.doctorVisitEventMapper = doctorVisitEventMapper;
        this.exerciseEventMapper = exerciseEventMapper;
        this.feedEventMapper = feedEventMapper;
        this.foodMapper = foodMapper;
        this.foodMeasureMapper = foodMeasureMapper;
        this.masterEventMapper = masterEventMapper;
        this.medicineTakingEventMapper = medicineTakingEventMapper;
        this.otherEventMapper = otherEventMapper;
        this.pumpEventMapper = pumpEventMapper;
        this.sleepEventMapper = sleepEventMapper;
    }

    public Observable<List<FoodMeasure>> getFoodMeasureList() {
        return dataStore.select(FoodMeasureEntity.class)
                .orderBy(FoodMeasureEntity.NAME, FoodMeasureEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, foodMeasureMapper));
    }

    public Observable<FoodMeasure> addFoodMeasure(@NonNull FoodMeasure foodMeasure) {
        return DbUtils.insertObservable(blockingEntityStore, foodMeasure, foodMeasureMapper);
    }

    public Observable<List<Food>> getFoodList() {
        return dataStore.select(FoodEntity.class)
                .orderBy(FoodEntity.NAME, FoodEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, foodMapper));
    }

    public Observable<Food> addFood(@NonNull Food food) {
        return DbUtils.insertObservable(blockingEntityStore, food, foodMapper);
    }

    public Observable<GetSleepEventsResponse> getSleepEvents(@NonNull GetSleepEventsRequest request) {
        JoinAndOr<ReactiveResult<SleepEventEntity>> select = dataStore.select(SleepEventEntity.class)
                .join(MasterEventEntity.class).on(SleepEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .and(MasterEventEntity.EVENT_TYPE.eq(EventType.SLEEP));

        if (request.getChild() != null && request.getChild().getId() != null) {
            select = select.and(MasterEventEntity.CHILD_ID.eq(request.getChild().getId()));
        }

        if (request.isWithStartedTimer()) {
            select = select.and(SleepEventEntity.TIMER_STARTED.eq(true));
        }

        return select
                .orderBy(MasterEventEntity.DATE_TIME, MasterEventEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, sleepEventMapper))
                .map(events -> GetSleepEventsResponse.builder().request(request).events(events).build());
    }

    public Observable<GetDoctorVisitEventsResponse> getDoctorVisitEvents(@NonNull GetDoctorVisitEventsRequest request) {
        JoinAndOr<ReactiveResult<DoctorVisitEventEntity>> select = dataStore.select(DoctorVisitEventEntity.class)
                .join(MasterEventEntity.class).on(DoctorVisitEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .and(MasterEventEntity.EVENT_TYPE.eq(EventType.DOCTOR_VISIT));

        if (request.getChild() != null && request.getChild().getId() != null) {
            select = select.and(MasterEventEntity.CHILD_ID.eq(request.getChild().getId()));
        }

        if (request.isWithImages()) {
            select = select.and(DoctorVisitEventEntity.IMAGE_FILE_NAME.notNull())
                    .and(DoctorVisitEventEntity.IMAGE_FILE_NAME.ne(""));
        }

        return select
                .orderBy(MasterEventEntity.DATE_TIME, MasterEventEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, doctorVisitEventMapper))
                .map(events -> GetDoctorVisitEventsResponse.builder().request(request).events(events).build());
    }

    public Observable<GetMedicineTakingEventsResponse> getMedicineTakingEvents(@NonNull GetMedicineTakingEventsRequest request) {
        JoinAndOr<ReactiveResult<MedicineTakingEventEntity>> select = dataStore.select(MedicineTakingEventEntity.class)
                .join(MasterEventEntity.class).on(MedicineTakingEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .and(MasterEventEntity.EVENT_TYPE.eq(EventType.MEDICINE_TAKING));

        if (request.getChild() != null && request.getChild().getId() != null) {
            select = select.and(MasterEventEntity.CHILD_ID.eq(request.getChild().getId()));
        }

        if (request.isWithImages()) {
            select = select.and(MedicineTakingEventEntity.IMAGE_FILE_NAME.notNull())
                    .and(MedicineTakingEventEntity.IMAGE_FILE_NAME.ne(""));
        }

        return select
                .orderBy(MasterEventEntity.DATE_TIME, MasterEventEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, medicineTakingEventMapper))
                .map(events -> GetMedicineTakingEventsResponse.builder().request(request).events(events).build());
    }

    public Observable<DiaperEvent> getDiaperEventDetail(@NonNull MasterEvent event) {
        return dataStore.select(DiaperEventEntity.class)
                .where(DiaperEventEntity.MASTER_EVENT_ID.eq(event.getMasterEventId()))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToObservable(reactiveResult, diaperEventMapper));
    }

    public Observable<FeedEvent> getFeedEventDetail(@NonNull MasterEvent event) {
        return dataStore.select(FeedEventEntity.class)
                .where(FeedEventEntity.MASTER_EVENT_ID.eq(event.getMasterEventId()))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToObservable(reactiveResult, feedEventMapper));
    }

    public Observable<OtherEvent> getOtherEventDetail(@NonNull MasterEvent event) {
        return dataStore.select(OtherEventEntity.class)
                .where(OtherEventEntity.MASTER_EVENT_ID.eq(event.getMasterEventId()))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToObservable(reactiveResult, otherEventMapper));
    }

    public Observable<PumpEvent> getPumpEventDetail(@NonNull MasterEvent event) {
        return dataStore.select(PumpEventEntity.class)
                .where(PumpEventEntity.MASTER_EVENT_ID.eq(event.getMasterEventId()))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToObservable(reactiveResult, pumpEventMapper));
    }

    public Observable<SleepEvent> getSleepEventDetail(@NonNull MasterEvent event) {
        return dataStore.select(SleepEventEntity.class)
                .where(SleepEventEntity.MASTER_EVENT_ID.eq(event.getMasterEventId()))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToObservable(reactiveResult, sleepEventMapper));
    }

    public Observable<DoctorVisitEvent> getDoctorVisitEventDetail(@NonNull MasterEvent event) {
        return dataStore.select(DoctorVisitEventEntity.class)
                .where(DoctorVisitEventEntity.MASTER_EVENT_ID.eq(event.getMasterEventId()))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToObservable(reactiveResult, doctorVisitEventMapper));
    }

    public Observable<MedicineTakingEvent> getMedicineTakingEventDetail(@NonNull MasterEvent event) {
        return dataStore.select(MedicineTakingEventEntity.class)
                .where(MedicineTakingEventEntity.MASTER_EVENT_ID.eq(event.getMasterEventId()))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToObservable(reactiveResult, medicineTakingEventMapper));
    }

    public Observable<ExerciseEvent> getExerciseEventDetail(@NonNull MasterEvent event) {
        return dataStore.select(ExerciseEventEntity.class)
                .where(ExerciseEventEntity.MASTER_EVENT_ID.eq(event.getMasterEventId()))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToObservable(reactiveResult, exerciseEventMapper));
    }

    public Observable<DiaperEvent> add(@NonNull DiaperEvent event) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            MasterEvent masterEvent = insertMasterEvent(event);
            DiaperEvent diaperEvent = event.toBuilder().masterEventId(masterEvent.getMasterEventId()).build();
            return DbUtils.insert(blockingEntityStore, diaperEvent, diaperEventMapper);
        }));
    }

    public Observable<FeedEvent> add(@NonNull FeedEvent event) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            MasterEvent masterEvent = insertMasterEvent(event);
            FeedEvent feedEvent = event.toBuilder().masterEventId(masterEvent.getMasterEventId()).build();
            return DbUtils.insert(blockingEntityStore, feedEvent, feedEventMapper);
        }));
    }

    public Observable<OtherEvent> add(@NonNull OtherEvent event) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            MasterEvent masterEvent = insertMasterEvent(event);
            OtherEvent otherEvent = event.toBuilder().masterEventId(masterEvent.getMasterEventId()).build();
            return DbUtils.insert(blockingEntityStore, otherEvent, otherEventMapper);
        }));
    }

    public Observable<PumpEvent> add(@NonNull PumpEvent event) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            MasterEvent masterEvent = insertMasterEvent(event);
            PumpEvent pumpEvent = event.toBuilder().masterEventId(masterEvent.getMasterEventId()).build();
            return DbUtils.insert(blockingEntityStore, pumpEvent, pumpEventMapper);
        }));
    }

    public Observable<SleepEvent> add(@NonNull SleepEvent event) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            MasterEvent masterEvent = insertMasterEvent(event);
            SleepEvent sleepEvent = event.toBuilder().masterEventId(masterEvent.getMasterEventId()).build();
            return DbUtils.insert(blockingEntityStore, sleepEvent, sleepEventMapper);
        }));
    }

    public Observable<DiaperEvent> update(@NonNull DiaperEvent event) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            updateMasterEvent(event);
            return DbUtils.update(blockingEntityStore, event, diaperEventMapper);
        }));
    }

    public Observable<FeedEvent> update(@NonNull FeedEvent event) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            updateMasterEvent(event);
            return DbUtils.update(blockingEntityStore, event, feedEventMapper);
        }));
    }

    public Observable<OtherEvent> update(@NonNull OtherEvent event) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            updateMasterEvent(event);
            return DbUtils.update(blockingEntityStore, event, otherEventMapper);
        }));
    }

    public Observable<PumpEvent> update(@NonNull PumpEvent event) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            updateMasterEvent(event);
            return DbUtils.update(blockingEntityStore, event, pumpEventMapper);
        }));
    }

    public Observable<SleepEvent> update(@NonNull SleepEvent event) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            updateMasterEvent(event);
            return DbUtils.update(blockingEntityStore, event, sleepEventMapper);
        }));
    }

    public Observable<UpdateDoctorVisitEventResponse> update(@NonNull UpdateDoctorVisitEventRequest request) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            DoctorVisitEvent doctorVisitEvent = request.getDoctorVisitEvent();
            List<LinearGroupFieldType> fields = request.getFields();
            int minutes = request.getMinutes();

            List<String> imageFilesToDelete = Collections.emptyList();

            if (minutes == 0) {
                imageFilesToDelete = updateDoctorVisitEvent(doctorVisitEvent);
            }

            updateLinearGroup(doctorVisitEvent, fields, minutes);

            DoctorVisitEventEntity doctorVisitEventEntity = blockingEntityStore
                    .select(DoctorVisitEventEntity.class)
                    .where(DoctorVisitEventEntity.ID.eq(doctorVisitEvent.getId()))
                    .get()
                    .first();
            DoctorVisitEvent result = doctorVisitEventMapper.mapToPlainObject(doctorVisitEventEntity);

            return UpdateDoctorVisitEventResponse.builder()
                    .request(request)
                    .doctorVisitEvent(result)
                    .imageFilesToDelete(imageFilesToDelete)
                    .build();
        }));
    }

    public Observable<UpdateMedicineTakingEventResponse> update(@NonNull UpdateMedicineTakingEventRequest request) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            MedicineTakingEvent medicineTakingEvent = request.getMedicineTakingEvent();
            List<LinearGroupFieldType> fields = request.getFields();
            int minutes = request.getMinutes();

            List<String> imageFilesToDelete = Collections.emptyList();

            if (minutes == 0) {
                imageFilesToDelete = updateMedicineTakingEvent(medicineTakingEvent);
            }

            updateLinearGroup(medicineTakingEvent, fields, minutes);

            MedicineTakingEventEntity medicineTakingEventEntity = blockingEntityStore
                    .select(MedicineTakingEventEntity.class)
                    .where(MedicineTakingEventEntity.ID.eq(medicineTakingEvent.getId()))
                    .get()
                    .first();
            MedicineTakingEvent result = medicineTakingEventMapper.mapToPlainObject(medicineTakingEventEntity);

            return UpdateMedicineTakingEventResponse.builder()
                    .request(request)
                    .medicineTakingEvent(result)
                    .imageFilesToDelete(imageFilesToDelete)
                    .build();
        }));
    }

    public Observable<UpdateExerciseEventResponse> update(@NonNull UpdateExerciseEventRequest request) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            ExerciseEvent exerciseEvent = request.getExerciseEvent();
            List<LinearGroupFieldType> fields = request.getFields();
            int minutes = request.getMinutes();

            List<String> imageFilesToDelete = Collections.emptyList();

            if (minutes == 0) {
                imageFilesToDelete = updateExerciseEvent(exerciseEvent);
            }

            updateLinearGroup(exerciseEvent, fields, minutes);

            ExerciseEventEntity exerciseEventEntity = blockingEntityStore
                    .select(ExerciseEventEntity.class)
                    .where(ExerciseEventEntity.ID.eq(exerciseEvent.getId()))
                    .get()
                    .first();
            ExerciseEvent result = exerciseEventMapper.mapToPlainObject(exerciseEventEntity);

            return UpdateExerciseEventResponse.builder()
                    .request(request)
                    .exerciseEvent(result)
                    .imageFilesToDelete(imageFilesToDelete)
                    .build();
        }));
    }

    private List<String> updateDoctorVisitEvent(DoctorVisitEvent doctorVisitEvent) {
        DoctorVisitEventEntity oldDoctorVisitEventEntity = blockingEntityStore
                .select(DoctorVisitEventEntity.class)
                .where(DoctorVisitEventEntity.ID.eq(doctorVisitEvent.getId()))
                .get()
                .first();

        List<String> imageFilesToDelete = new ArrayList<>();
        if (!TextUtils.isEmpty(oldDoctorVisitEventEntity.getImageFileName())
                && !oldDoctorVisitEventEntity.getImageFileName().equals(doctorVisitEvent.getImageFileName())) {
            imageFilesToDelete.add(oldDoctorVisitEventEntity.getImageFileName());
        }

        updateMasterEvent(doctorVisitEvent);
        DoctorVisitEvent result = DbUtils.update(blockingEntityStore, doctorVisitEvent, doctorVisitEventMapper);

        return imageFilesToDelete;
    }

    private List<String> updateMedicineTakingEvent(MedicineTakingEvent medicineTakingEvent) {
        MedicineTakingEventEntity oldMedicineTakingEventEntity = blockingEntityStore
                .select(MedicineTakingEventEntity.class)
                .where(MedicineTakingEventEntity.ID.eq(medicineTakingEvent.getId()))
                .get()
                .first();

        List<String> imageFilesToDelete = new ArrayList<>();
        if (!TextUtils.isEmpty(oldMedicineTakingEventEntity.getImageFileName())
                && !oldMedicineTakingEventEntity.getImageFileName().equals(medicineTakingEvent.getImageFileName())) {
            imageFilesToDelete.add(oldMedicineTakingEventEntity.getImageFileName());
        }

        updateMasterEvent(medicineTakingEvent);
        MedicineTakingEvent result = DbUtils.update(blockingEntityStore, medicineTakingEvent, medicineTakingEventMapper);

        return imageFilesToDelete;
    }

    private List<String> updateExerciseEvent(ExerciseEvent exerciseEvent) {
        ExerciseEventEntity oldExerciseEventEntity = blockingEntityStore
                .select(ExerciseEventEntity.class)
                .where(ExerciseEventEntity.ID.eq(exerciseEvent.getId()))
                .get()
                .first();

        List<String> imageFilesToDelete = new ArrayList<>();
        if (!TextUtils.isEmpty(oldExerciseEventEntity.getImageFileName())
                && !oldExerciseEventEntity.getImageFileName().equals(exerciseEvent.getImageFileName())) {
            imageFilesToDelete.add(oldExerciseEventEntity.getImageFileName());
        }

        updateMasterEvent(exerciseEvent);
        ExerciseEvent result = DbUtils.update(blockingEntityStore, exerciseEvent, exerciseEventMapper);

        return imageFilesToDelete;
    }

    private void updateLinearGroup(@NonNull DoctorVisitEvent doctorVisitEvent,
                                   @NonNull List<LinearGroupFieldType> fields,
                                   int minutes) {
        if (fields.isEmpty()) {
            logger.debug("changed fields list is empty");
            return;
        }

        Integer linearGroup = doctorVisitEvent.getLinearGroup();
        if (linearGroup == null) {
            logger.error("no linear group: " + doctorVisitEvent);
            return;
        }

        Long doctorVisitId = getDoctorVisitId(doctorVisitEvent);
        List<DoctorVisitEventEntity> events = getDoctorVisitEvents(doctorVisitId, linearGroup);

        if (fields.contains(LinearGroupFieldType.DOCTOR)
                || fields.contains(LinearGroupFieldType.DOCTOR_VISIT_EVENT_NAME)
                || fields.contains(LinearGroupFieldType.DOCTOR_VISIT_EVENT_DURATION_IN_MINUTES)) {

            for (DoctorVisitEventEntity event : events) {
                for (LinearGroupFieldType field : fields) {
                    updateEventField(doctorVisitEvent, event, field);
                }
            }

            blockingEntityStore.update(events);
        }

        if (fields.contains(LinearGroupFieldType.NOTIFY_TIME_IN_MINUTES)
                || fields.contains(LinearGroupFieldType.TIME)) {

            List<MasterEventEntity> masterEvents = getMasterEventsFromDoctorVisitEvents(events);

            for (MasterEventEntity masterEvent : masterEvents) {
                for (LinearGroupFieldType field : fields) {
                    updateEventField(doctorVisitEvent, masterEvent, field, minutes);
                }
            }

            blockingEntityStore.update(masterEvents);
        }
    }

    private void updateLinearGroup(@NonNull MedicineTakingEvent medicineTakingEvent,
                                   @NonNull List<LinearGroupFieldType> fields,
                                   int minutes) {
        if (fields.isEmpty()) {
            logger.debug("changed fields list is empty");
            return;
        }

        Integer linearGroup = medicineTakingEvent.getLinearGroup();
        if (linearGroup == null) {
            logger.error("no linear group: " + medicineTakingEvent);
            return;
        }

        Long medicineTakingId = getMedicineTakingId(medicineTakingEvent);
        List<MedicineTakingEventEntity> events = getMedicineTakingEvents(medicineTakingId, linearGroup);

        if (fields.contains(LinearGroupFieldType.MEDICINE)
                || fields.contains(LinearGroupFieldType.AMOUNT)
                || fields.contains(LinearGroupFieldType.MEDICINE_MEASURE)) {

            for (MedicineTakingEventEntity event : events) {
                for (LinearGroupFieldType field : fields) {
                    updateEventField(medicineTakingEvent, event, field);
                }
            }

            blockingEntityStore.update(events);
        }

        if (fields.contains(LinearGroupFieldType.NOTIFY_TIME_IN_MINUTES)
                || fields.contains(LinearGroupFieldType.TIME)) {
            List<MasterEventEntity> masterEvents = getMasterEventsFromMedicineTakingEvents(events);

            for (MasterEventEntity masterEvent : masterEvents) {
                for (LinearGroupFieldType field : fields) {
                    updateEventField(medicineTakingEvent, masterEvent, field, minutes);
                }
            }

            blockingEntityStore.update(masterEvents);
        }
    }

    private void updateLinearGroup(@NonNull ExerciseEvent exerciseEvent,
                                   @NonNull List<LinearGroupFieldType> fields,
                                   int minutes) {
        if (fields.isEmpty()) {
            logger.debug("changed fields list is empty");
            return;
        }

        Integer linearGroup = exerciseEvent.getLinearGroup();
        if (linearGroup == null) {
            logger.error("no linear group: " + exerciseEvent);
            return;
        }

        Long concreteExerciseId = getConcreteExerciseId(exerciseEvent);
        List<ExerciseEventEntity> events = getExerciseEvents(concreteExerciseId, linearGroup);

        if (fields.contains(LinearGroupFieldType.EXERCISE_EVENT_NAME)
                || fields.contains(LinearGroupFieldType.EXERCISE_EVENT_DURATION_IN_MINUTES)) {

            for (ExerciseEventEntity event : events) {
                for (LinearGroupFieldType field : fields) {
                    updateEventField(exerciseEvent, event, field);
                }
            }

            blockingEntityStore.update(events);
        }

        if (fields.contains(LinearGroupFieldType.NOTIFY_TIME_IN_MINUTES)
                || fields.contains(LinearGroupFieldType.TIME)) {

            List<MasterEventEntity> masterEvents = getMasterEventsFromExerciseEvents(events);

            for (MasterEventEntity masterEvent : masterEvents) {
                for (LinearGroupFieldType field : fields) {
                    updateEventField(exerciseEvent, masterEvent, field, minutes);
                }
            }

            blockingEntityStore.update(masterEvents);
        }
    }

    private void updateEventField(@NonNull DoctorVisitEvent from,
                                  @NonNull DoctorVisitEventEntity to,
                                  @NonNull LinearGroupFieldType field) {
        switch (field) {
            case DOCTOR:
                DoctorEntity doctorEntity = findDoctorEntity(from.getDoctor());
                to.setDoctor(doctorEntity);
                break;
            case DOCTOR_VISIT_EVENT_NAME:
                String name = from.getName();
                to.setName(name);
                break;
            case DOCTOR_VISIT_EVENT_DURATION_IN_MINUTES:
                Integer durationInMinutes = from.getDurationInMinutes();
                to.setDurationInMinutes(durationInMinutes);
                break;
        }
    }

    private void updateEventField(@NonNull MedicineTakingEvent from,
                                  @NonNull MedicineTakingEventEntity to,
                                  @NonNull LinearGroupFieldType field) {
        switch (field) {
            case MEDICINE:
                MedicineEntity medicineEntity = findMedicineEntity(from.getMedicine());
                to.setMedicine(medicineEntity);
                break;
            case AMOUNT:
                Double amount = from.getAmount();
                to.setAmount(amount);
                break;
            case MEDICINE_MEASURE:
                MedicineMeasureEntity medicineMeasureEntity = findMedicineMeasureEntity(from.getMedicineMeasure());
                to.setMedicineMeasure(medicineMeasureEntity);
                break;
        }
    }

    private void updateEventField(@NonNull ExerciseEvent from,
                                  @NonNull ExerciseEventEntity to,
                                  @NonNull LinearGroupFieldType field) {
        switch (field) {
            case EXERCISE_EVENT_NAME:
                String name = from.getName();
                to.setName(name);
                break;
            case EXERCISE_EVENT_DURATION_IN_MINUTES:
                Integer durationInMinutes = from.getDurationInMinutes();
                to.setDurationInMinutes(durationInMinutes);
                break;
        }
    }

    private void updateEventField(@NonNull MasterEvent from,
                                  @NonNull MasterEventEntity to,
                                  @NonNull LinearGroupFieldType field,
                                  int minutes) {
        switch (field) {
            case NOTIFY_TIME_IN_MINUTES:
                Integer notifyTimeInMinutes = from.getNotifyTimeInMinutes();
                to.setNotifyTimeInMinutes(notifyTimeInMinutes);
                break;
            case TIME:
                DateTime dateTime = to.getDateTime().plusMinutes(minutes);
                to.setDateTime(dateTime);
                break;
        }
    }

    private MasterEvent insertMasterEvent(@NonNull MasterEvent event) {
        return DbUtils.insert(blockingEntityStore, event, masterEventMapper);
    }

    public MasterEvent updateMasterEvent(@NonNull MasterEvent event) {
        return DbUtils.update(blockingEntityStore, event, masterEventMapper);
    }

    public Observable<MasterEvent> updateMasterEventObservable(@NonNull MasterEvent event) {
        return DbUtils.updateObservable(blockingEntityStore, event, masterEventMapper);
    }
}
