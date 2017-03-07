package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.android.childdiary.domain.interactors.calendar.CalendarRepository;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.child.Child;

@Singleton
public class CalendarDataRepository implements CalendarRepository {
    private final CalendarDbService dbService;

    @Inject
    public CalendarDataRepository(CalendarDbService dbService) {
        this.dbService = dbService;
    }

    @Override
    public Observable<List<MasterEvent>> getAll(@NonNull Child child, @NonNull LocalDate selectedDate) {
        return dbService.getAll(child, selectedDate);
    }

    @Override
    public Observable<DiaperEvent> getDiaperEventDetail(@NonNull MasterEvent event) {
        return dbService.getDiaperEventDetail(event);
    }

    @Override
    public Observable<FeedEvent> getFeedEventDetail(@NonNull MasterEvent event) {
        return dbService.getFeedEventDetail(event);
    }

    @Override
    public Observable<OtherEvent> getOtherEventDetail(@NonNull MasterEvent event) {
        return dbService.getOtherEventDetail(event);
    }

    @Override
    public Observable<PumpEvent> getPumpEventDetail(@NonNull MasterEvent event) {
        return dbService.getPumpEventDetail(event);
    }

    @Override
    public Observable<SleepEvent> getSleepEventDetail(@NonNull MasterEvent event) {
        return dbService.getSleepEventDetail(event);
    }

    @Override
    public Observable<DiaperEvent> add(@NonNull Child child, @NonNull DiaperEvent event) {
        return dbService.add(child, event);
    }

    @Override
    public Observable<FeedEvent> add(@NonNull Child child, @NonNull FeedEvent event) {
        return dbService.add(child, event);
    }

    @Override
    public Observable<OtherEvent> add(@NonNull Child child, @NonNull OtherEvent event) {
        return dbService.add(child, event);
    }

    @Override
    public Observable<PumpEvent> add(@NonNull Child child, @NonNull PumpEvent event) {
        return dbService.add(child, event);
    }

    @Override
    public Observable<SleepEvent> add(@NonNull Child child, @NonNull SleepEvent event) {
        return dbService.add(child, event);
    }

    @Override
    public Observable<DiaperEvent> update(@NonNull DiaperEvent event) {
        return dbService.update(event);
    }

    @Override
    public Observable<FeedEvent> update(@NonNull FeedEvent event) {
        return dbService.update(event);
    }

    @Override
    public Observable<OtherEvent> update(@NonNull OtherEvent event) {
        return dbService.update(event);
    }

    @Override
    public Observable<PumpEvent> update(@NonNull PumpEvent event) {
        return dbService.update(event);
    }

    @Override
    public Observable<SleepEvent> update(@NonNull SleepEvent event) {
        return dbService.update(event);
    }

    @Override
    public Observable<DiaperEvent> delete(@NonNull DiaperEvent event) {
        return dbService.delete(event);
    }

    @Override
    public Observable<FeedEvent> delete(@NonNull FeedEvent event) {
        return dbService.delete(event);
    }

    @Override
    public Observable<OtherEvent> delete(@NonNull OtherEvent event) {
        return dbService.delete(event);
    }

    @Override
    public Observable<PumpEvent> delete(@NonNull PumpEvent event) {
        return dbService.delete(event);
    }

    @Override
    public Observable<SleepEvent> delete(@NonNull SleepEvent event) {
        return dbService.delete(event);
    }
}
