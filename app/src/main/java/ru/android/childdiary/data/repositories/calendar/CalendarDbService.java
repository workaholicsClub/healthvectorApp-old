package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.entities.calendar.events.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.DiaperEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.FeedEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.OtherEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.PumpEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.SleepEventEntity;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.child.Child;

@Singleton
public class CalendarDbService implements CalendarService {
    private final ReactiveEntityStore<Persistable> dataStore;

    @Inject
    public CalendarDbService(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public Observable<List<MasterEvent>> getAll(@NonNull Child child, @NonNull LocalDate selectedDate) {
        return dataStore.select(MasterEventEntity.class)
                .where(MasterEventEntity.CHILD_ID.eq(child.getId()))
                .orderBy(MasterEventEntity.DATE_TIME)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, MasterEventMapper::mapToPlainObject));
    }

    @Override
    public Observable<DiaperEvent> getDiaperEventDetail(@NonNull MasterEvent event) {
        return dataStore.select(DiaperEventEntity.class)
                .where(DiaperEventEntity.MASTER_EVENT_ID.eq(event.getMasterEventId()))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToObservable(reactiveResult, DiaperEventMapper::mapToPlainObject));
    }

    @Override
    public Observable<FeedEvent> getFeedEventDetail(@NonNull MasterEvent event) {
        return dataStore.select(FeedEventEntity.class)
                .where(FeedEventEntity.MASTER_EVENT_ID.eq(event.getMasterEventId()))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToObservable(reactiveResult, FeedEventMapper::mapToPlainObject));
    }

    @Override
    public Observable<OtherEvent> getOtherEventDetail(@NonNull MasterEvent event) {
        return dataStore.select(OtherEventEntity.class)
                .where(OtherEventEntity.MASTER_EVENT_ID.eq(event.getMasterEventId()))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToObservable(reactiveResult, OtherEventMapper::mapToPlainObject));
    }

    @Override
    public Observable<PumpEvent> getPumpEventDetail(@NonNull MasterEvent event) {
        return dataStore.select(PumpEventEntity.class)
                .where(PumpEventEntity.MASTER_EVENT_ID.eq(event.getMasterEventId()))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToObservable(reactiveResult, PumpEventMapper::mapToPlainObject));
    }

    @Override
    public Observable<SleepEvent> getSleepEventDetail(@NonNull MasterEvent event) {
        return dataStore.select(SleepEventEntity.class)
                .where(SleepEventEntity.MASTER_EVENT_ID.eq(event.getMasterEventId()))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToObservable(reactiveResult, SleepEventMapper::mapToPlainObject));
    }

    private Observable<MasterEvent> add(@NonNull Child child, @NonNull MasterEvent event) {
        return DbUtils.addObservable(dataStore, ChildEntity.class, child.getId(), event,
                MasterEventMapper::mapToEntity, MasterEventMapper::mapToPlainObject);
    }

    @Override
    public Observable<DiaperEvent> add(@NonNull Child child, @NonNull DiaperEvent event) {
        return add(child, event.getMasterEvent())
                .flatMap(masterEvent -> DbUtils.addObservable(dataStore, MasterEventEntity.class, masterEvent.getMasterEventId(), event,
                        DiaperEventMapper::mapToEntity, DiaperEventMapper::mapToPlainObject));
    }

    @Override
    public Observable<FeedEvent> add(@NonNull Child child, @NonNull FeedEvent event) {
        return add(child, event.getMasterEvent())
                .flatMap(masterEvent -> DbUtils.addObservable(dataStore, MasterEventEntity.class, masterEvent.getMasterEventId(), event,
                        FeedEventMapper::mapToEntity, FeedEventMapper::mapToPlainObject));
    }

    @Override
    public Observable<OtherEvent> add(@NonNull Child child, @NonNull OtherEvent event) {
        return add(child, event.getMasterEvent())
                .flatMap(masterEvent -> DbUtils.addObservable(dataStore, MasterEventEntity.class, masterEvent.getMasterEventId(), event,
                        OtherEventMapper::mapToEntity, OtherEventMapper::mapToPlainObject));
    }

    @Override
    public Observable<PumpEvent> add(@NonNull Child child, @NonNull PumpEvent event) {
        return add(child, event.getMasterEvent())
                .flatMap(masterEvent -> DbUtils.addObservable(dataStore, MasterEventEntity.class, masterEvent.getMasterEventId(), event,
                        PumpEventMapper::mapToEntity, PumpEventMapper::mapToPlainObject));
    }

    @Override
    public Observable<SleepEvent> add(@NonNull Child child, @NonNull SleepEvent event) {
        return add(child, event.getMasterEvent())
                .flatMap(masterEvent -> DbUtils.addObservable(dataStore, MasterEventEntity.class, masterEvent.getMasterEventId(), event,
                        SleepEventMapper::mapToEntity, SleepEventMapper::mapToPlainObject));
    }

    @Override
    public Observable<DiaperEvent> update(@NonNull DiaperEvent event) {
        return DbUtils.updateObservable(dataStore, DiaperEventEntity.class, event, event.getId(),
                DiaperEventMapper::updateEntityWithPlainObject, DiaperEventMapper::mapToPlainObject);
    }

    @Override
    public Observable<FeedEvent> update(@NonNull FeedEvent event) {
        return DbUtils.updateObservable(dataStore, FeedEventEntity.class, event, event.getId(),
                FeedEventMapper::updateEntityWithPlainObject, FeedEventMapper::mapToPlainObject);
    }

    @Override
    public Observable<OtherEvent> update(@NonNull OtherEvent event) {
        return DbUtils.updateObservable(dataStore, OtherEventEntity.class, event, event.getId(),
                OtherEventMapper::updateEntityWithPlainObject, OtherEventMapper::mapToPlainObject);
    }

    @Override
    public Observable<PumpEvent> update(@NonNull PumpEvent event) {
        return DbUtils.updateObservable(dataStore, PumpEventEntity.class, event, event.getId(),
                PumpEventMapper::updateEntityWithPlainObject, PumpEventMapper::mapToPlainObject);
    }

    @Override
    public Observable<SleepEvent> update(@NonNull SleepEvent event) {
        return DbUtils.updateObservable(dataStore, SleepEventEntity.class, event, event.getId(),
                SleepEventMapper::updateEntityWithPlainObject, SleepEventMapper::mapToPlainObject);
    }

    @Override
    public Observable<DiaperEvent> delete(@NonNull DiaperEvent event) {
        event = event.toBuilder().isDeleted(true).build();
        return DbUtils.updateObservable(dataStore, DiaperEventEntity.class, event, event.getId(),
                DiaperEventMapper::updateEntityWithPlainObject, DiaperEventMapper::mapToPlainObject);
    }

    @Override
    public Observable<FeedEvent> delete(@NonNull FeedEvent event) {
        event = event.toBuilder().isDeleted(true).build();
        return DbUtils.updateObservable(dataStore, FeedEventEntity.class, event, event.getId(),
                FeedEventMapper::updateEntityWithPlainObject, FeedEventMapper::mapToPlainObject);
    }

    @Override
    public Observable<OtherEvent> delete(@NonNull OtherEvent event) {
        event = event.toBuilder().isDeleted(true).build();
        return DbUtils.updateObservable(dataStore, OtherEventEntity.class, event, event.getId(),
                OtherEventMapper::updateEntityWithPlainObject, OtherEventMapper::mapToPlainObject);
    }

    @Override
    public Observable<PumpEvent> delete(@NonNull PumpEvent event) {
        event = event.toBuilder().isDeleted(true).build();
        return DbUtils.updateObservable(dataStore, PumpEventEntity.class, event, event.getId(),
                PumpEventMapper::updateEntityWithPlainObject, PumpEventMapper::mapToPlainObject);
    }

    @Override
    public Observable<SleepEvent> delete(@NonNull SleepEvent event) {
        event = event.toBuilder().isDeleted(true).build();
        return DbUtils.updateObservable(dataStore, SleepEventEntity.class, event, event.getId(),
                SleepEventMapper::updateEntityWithPlainObject, SleepEventMapper::mapToPlainObject);
    }
}
