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
        // TODO
        return null;
    }

    @Override
    public Observable<FeedEvent> getFeedEventDetail(@NonNull MasterEvent event) {
        // TODO
        return null;
    }

    @Override
    public Observable<OtherEvent> getOtherEventDetail(@NonNull MasterEvent event) {
        // TODO
        return null;
    }

    @Override
    public Observable<PumpEvent> getPumpEventDetail(@NonNull MasterEvent event) {
        // TODO
        return null;
    }

    @Override
    public Observable<SleepEvent> getSleepEventDetail(@NonNull MasterEvent event) {
        // TODO
        return null;
    }

    private Observable<MasterEvent> add(@NonNull Child child, @NonNull MasterEvent event) {
        return DbUtils.addObservable(dataStore, ChildEntity.class, child.getId(), event,
                MasterEventMapper::mapToEntity, MasterEventMapper::mapToPlainObject);
    }

    @Override
    public Observable<DiaperEvent> add(@NonNull Child child, @NonNull DiaperEvent event) {
        return add(child, event.getMasterEvent())
                .flatMap(masterEvent -> DbUtils.addObservable(dataStore, MasterEventEntity.class, masterEvent.getId(), event,
                        DiaperEventMapper::mapToEntity, DiaperEventMapper::mapToPlainObject));
    }

    @Override
    public Observable<FeedEvent> add(@NonNull Child child, @NonNull FeedEvent event) {
        // TODO
        return null;
    }

    @Override
    public Observable<OtherEvent> add(@NonNull Child child, @NonNull OtherEvent event) {
        // TODO
        return null;
    }

    @Override
    public Observable<PumpEvent> add(@NonNull Child child, @NonNull PumpEvent event) {
        // TODO
        return null;
    }

    @Override
    public Observable<SleepEvent> add(@NonNull Child child, @NonNull SleepEvent event) {
        // TODO
        return null;
    }

    @Override
    public Observable<DiaperEvent> update(@NonNull DiaperEvent event) {
        // TODO
        return null;
    }

    @Override
    public Observable<FeedEvent> update(@NonNull FeedEvent event) {
        // TODO
        return null;
    }

    @Override
    public Observable<OtherEvent> update(@NonNull OtherEvent event) {
        // TODO
        return null;
    }

    @Override
    public Observable<PumpEvent> update(@NonNull PumpEvent event) {
        // TODO
        return null;
    }

    @Override
    public Observable<SleepEvent> update(@NonNull SleepEvent event) {
        // TODO
        return null;
    }

    @Override
    public Observable<DiaperEvent> delete(@NonNull DiaperEvent event) {
        // TODO
        return null;
    }

    @Override
    public Observable<FeedEvent> delete(@NonNull FeedEvent event) {
        // TODO
        return null;
    }

    @Override
    public Observable<OtherEvent> delete(@NonNull OtherEvent event) {
        // TODO
        return null;
    }

    @Override
    public Observable<PumpEvent> delete(@NonNull PumpEvent event) {
        // TODO
        return null;
    }

    @Override
    public Observable<SleepEvent> delete(@NonNull SleepEvent event) {
        // TODO
        return null;
    }
}
