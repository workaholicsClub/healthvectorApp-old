package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;

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
import ru.android.childdiary.data.entities.calendar.events.standard.DiaperEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.FeedEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.OtherEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.PumpEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.SleepEventEntity;
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

@Singleton
public class CalendarDbService {
    private final ReactiveEntityStore<Persistable> dataStore;
    private final DiaperEventMapper diaperEventMapper;
    private final DoctorVisitEventMapper doctorVisitEventMapper;
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
                             MasterEventMapper masterEventMapper,
                             MedicineTakingEventMapper medicineTakingEventMapper,
                             OtherEventMapper otherEventMapper,
                             PumpEventMapper pumpEventMapper,
                             SleepEventMapper sleepEventMapper) {
        this.dataStore = dataStore;
        this.diaperEventMapper = diaperEventMapper;
        this.doctorVisitEventMapper = doctorVisitEventMapper;
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
        return DbUtils.insertObservable(dataStore, foodMeasure, foodMeasureMapper);
    }

    public Observable<List<Food>> getFoodList() {
        return dataStore.select(FoodEntity.class)
                .orderBy(FoodEntity.NAME, FoodEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, foodMapper));
    }

    public Observable<Food> addFood(@NonNull Food food) {
        return DbUtils.insertObservable(dataStore, food, foodMapper);
    }

    public Observable<List<SleepEvent>> getSleepEventsWithTimer() {
        return dataStore.select(SleepEventEntity.class)
                .join(MasterEventEntity.class).on(SleepEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .and(MasterEventEntity.EVENT_TYPE.eq(EventType.SLEEP))
                .and(SleepEventEntity.TIMER_STARTED.eq(true))
                .orderBy(MasterEventEntity.DATE_TIME, MasterEventEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, sleepEventMapper));
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

    public Observable<DiaperEvent> add(@NonNull DiaperEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            MasterEvent masterEvent = insertMasterEvent(event);
            DiaperEvent diaperEvent = event.toBuilder().masterEventId(masterEvent.getMasterEventId()).build();
            return DbUtils.insert(dataStore, diaperEvent, diaperEventMapper);
        }));
    }

    public Observable<FeedEvent> add(@NonNull FeedEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            MasterEvent masterEvent = insertMasterEvent(event);
            FeedEvent feedEvent = event.toBuilder().masterEventId(masterEvent.getMasterEventId()).build();
            return DbUtils.insert(dataStore, feedEvent, feedEventMapper);
        }));
    }

    public Observable<OtherEvent> add(@NonNull OtherEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            MasterEvent masterEvent = insertMasterEvent(event);
            OtherEvent otherEvent = event.toBuilder().masterEventId(masterEvent.getMasterEventId()).build();
            return DbUtils.insert(dataStore, otherEvent, otherEventMapper);
        }));
    }

    public Observable<PumpEvent> add(@NonNull PumpEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            MasterEvent masterEvent = insertMasterEvent(event);
            PumpEvent pumpEvent = event.toBuilder().masterEventId(masterEvent.getMasterEventId()).build();
            return DbUtils.insert(dataStore, pumpEvent, pumpEventMapper);
        }));
    }

    public Observable<SleepEvent> add(@NonNull SleepEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            MasterEvent masterEvent = insertMasterEvent(event);
            SleepEvent sleepEvent = event.toBuilder().masterEventId(masterEvent.getMasterEventId()).build();
            return DbUtils.insert(dataStore, sleepEvent, sleepEventMapper);
        }));
    }

    public Observable<DiaperEvent> update(@NonNull DiaperEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            updateMasterEvent(event);
            return DbUtils.update(dataStore, event, diaperEventMapper);
        }));
    }

    public Observable<FeedEvent> update(@NonNull FeedEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            updateMasterEvent(event);
            return DbUtils.update(dataStore, event, feedEventMapper);
        }));
    }

    public Observable<OtherEvent> update(@NonNull OtherEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            updateMasterEvent(event);
            return DbUtils.update(dataStore, event, otherEventMapper);
        }));
    }

    public Observable<PumpEvent> update(@NonNull PumpEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            updateMasterEvent(event);
            return DbUtils.update(dataStore, event, pumpEventMapper);
        }));
    }

    public Observable<SleepEvent> update(@NonNull SleepEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            updateMasterEvent(event);
            return DbUtils.update(dataStore, event, sleepEventMapper);
        }));
    }

    public Observable<DoctorVisitEvent> update(@NonNull DoctorVisitEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            updateMasterEvent(event);
            return DbUtils.update(dataStore, event, doctorVisitEventMapper);
        }));
    }

    public Observable<MedicineTakingEvent> update(@NonNull MedicineTakingEvent event) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            updateMasterEvent(event);
            return DbUtils.update(dataStore, event, medicineTakingEventMapper);
        }));
    }

    public <T extends MasterEvent> Observable<T> delete(@NonNull T event) {
        return DbUtils.deleteObservable(dataStore, MasterEventEntity.class, event, event.getMasterEventId());
    }

    public Observable<MasterEvent> done(@NonNull MasterEvent event) {
        MasterEvent masterEvent = event.toMasterBuilder().isDone(!event.getIsDone()).build();
        return DbUtils.updateObservable(dataStore, masterEvent, masterEventMapper);
    }

    private MasterEvent insertMasterEvent(@NonNull MasterEvent event) {
        return DbUtils.insert(dataStore, event, masterEventMapper);
    }

    private MasterEvent updateMasterEvent(@NonNull MasterEvent event) {
        return DbUtils.update(dataStore, event, masterEventMapper);
    }
}
