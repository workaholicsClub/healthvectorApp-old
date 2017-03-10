package ru.android.childdiary.domain.interactors.calendar;

import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.calendar.CalendarDataRepository;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.core.Interactor;
import ru.android.childdiary.domain.core.events.SelectedDateChangedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.child.Child;

public class CalendarInteractor implements Interactor, CalendarRepository {
    private final Logger logger = LoggerFactory.getLogger(toString());

    private final EventBus bus;
    private final CalendarDataRepository calendarRepository;

    @Inject
    public CalendarInteractor(EventBus bus, CalendarDataRepository calendarRepository) {
        this.bus = bus;
        this.calendarRepository = calendarRepository;
    }

    public Observable<LocalDate> setSelectedDate(@NonNull LocalDate date) {
        return Observable.fromCallable(() -> {
            bus.post(SelectedDateChangedEvent.builder().date(date).build());
            return date;
        });
    }

    @Override
    public Observable<List<MasterEvent>> getAll(@NonNull Child child, @NonNull LocalDate selectedDate) {
        return calendarRepository.getAll(child, selectedDate);
    }

    @Override
    public Observable<DiaperEvent> getDiaperEventDetail(@NonNull MasterEvent event) {
        return calendarRepository.getDiaperEventDetail(event);
    }

    @Override
    public Observable<FeedEvent> getFeedEventDetail(@NonNull MasterEvent event) {
        return calendarRepository.getFeedEventDetail(event);
    }

    @Override
    public Observable<OtherEvent> getOtherEventDetail(@NonNull MasterEvent event) {
        return calendarRepository.getOtherEventDetail(event);
    }

    @Override
    public Observable<PumpEvent> getPumpEventDetail(@NonNull MasterEvent event) {
        return calendarRepository.getPumpEventDetail(event);
    }

    @Override
    public Observable<SleepEvent> getSleepEventDetail(@NonNull MasterEvent event) {
        return calendarRepository.getSleepEventDetail(event);
    }

    @Override
    public Observable<DiaperEvent> add(@NonNull Child child, @NonNull DiaperEvent event) {
        event = event.toBuilder().eventType(EventType.DIAPER).build();
        return calendarRepository.add(child, event);
    }

    @Override
    public Observable<FeedEvent> add(@NonNull Child child, @NonNull FeedEvent event) {
        event = event.toBuilder().eventType(EventType.FEED).build();
        return calendarRepository.add(child, event);
    }

    @Override
    public Observable<OtherEvent> add(@NonNull Child child, @NonNull OtherEvent event) {
        event = event.toBuilder().eventType(EventType.OTHER).build();
        return calendarRepository.add(child, event);
    }

    @Override
    public Observable<PumpEvent> add(@NonNull Child child, @NonNull PumpEvent event) {
        event = event.toBuilder().eventType(EventType.PUMP).build();
        return calendarRepository.add(child, event);
    }

    @Override
    public Observable<SleepEvent> add(@NonNull Child child, @NonNull SleepEvent event) {
        event = event.toBuilder().eventType(EventType.SLEEP).build();
        return calendarRepository.add(child, event);
    }

    @Override
    public Observable<DiaperEvent> update(@NonNull DiaperEvent event) {
        return calendarRepository.update(event);
    }

    @Override
    public Observable<FeedEvent> update(@NonNull FeedEvent event) {
        return calendarRepository.update(event);
    }

    @Override
    public Observable<OtherEvent> update(@NonNull OtherEvent event) {
        return calendarRepository.update(event);
    }

    @Override
    public Observable<PumpEvent> update(@NonNull PumpEvent event) {
        return calendarRepository.update(event);
    }

    @Override
    public Observable<SleepEvent> update(@NonNull SleepEvent event) {
        return calendarRepository.update(event);
    }

    @Override
    public Observable<DiaperEvent> delete(@NonNull DiaperEvent event) {
        return calendarRepository.delete(event);
    }

    @Override
    public Observable<FeedEvent> delete(@NonNull FeedEvent event) {
        return calendarRepository.delete(event);
    }

    @Override
    public Observable<OtherEvent> delete(@NonNull OtherEvent event) {
        return calendarRepository.delete(event);
    }

    @Override
    public Observable<PumpEvent> delete(@NonNull PumpEvent event) {
        return calendarRepository.delete(event);
    }

    @Override
    public Observable<SleepEvent> delete(@NonNull SleepEvent event) {
        return calendarRepository.delete(event);
    }
}
