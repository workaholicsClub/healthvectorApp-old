package ru.android.childdiary.domain.interactors.calendar;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.calendar.CalendarDataRepository;
import ru.android.childdiary.data.types.Breast;
import ru.android.childdiary.data.types.DiaperState;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.domain.core.Interactor;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.calendar.requests.AddEventRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.EventsRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.EventsResponse;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;

public class CalendarInteractor implements Interactor {
    private final Logger logger = LoggerFactory.getLogger(toString());

    private final CalendarDataRepository calendarRepository;
    private final ChildInteractor childInteractor;

    @Inject
    public CalendarInteractor(CalendarDataRepository calendarRepository, ChildInteractor childInteractor) {
        this.calendarRepository = calendarRepository;
        this.childInteractor = childInteractor;
    }

    public Observable<LocalDate> getSelectedDate() {
        return calendarRepository.getSelectedDate();
    }

    public Observable<LocalDate> getSelectedDateOnce() {
        return calendarRepository.getSelectedDate().first(LocalDate.now()).toObservable();
    }

    public Observable<LocalDate> setSelectedDate(@NonNull LocalDate date) {
        return calendarRepository.setSelectedDate(date);
    }

    public Observable<List<FoodMeasure>> getFoodMeasureList() {
        return calendarRepository.getFoodMeasureList();
    }

    public Observable<FoodMeasure> addFoodMeasure(@NonNull FoodMeasure foodMeasure) {
        return calendarRepository.addFoodMeasure(foodMeasure);
    }

    private Observable<FoodMeasure> getDefaultFoodMeasure() {
        return calendarRepository.getFoodMeasureList()
                .first(Collections.singletonList(FoodMeasure.NULL))
                .flatMapObservable(Observable::fromIterable)
                .first(FoodMeasure.NULL)
                .toObservable();
    }

    private Observable<Integer> getDefaultNotifyTimeInMinutes(@NonNull EventType eventType) {
        switch (eventType) {
            case DIAPER:
                return Observable.just(5);
            case FEED:
                return Observable.just(10);
            case OTHER:
                return Observable.just(60);
            case PUMP:
                return Observable.just(20);
            case SLEEP:
                return Observable.just(10);
        }
        throw new IllegalArgumentException("Unknown event type");
    }

    @SuppressWarnings("unchecked")
    public <T extends MasterEvent> Observable<T> getDefaultEventDetail(@NonNull EventType eventType) {
        switch (eventType) {
            case DIAPER:
                return (Observable<T>) Observable.combineLatest(
                        childInteractor.getActiveChildOnce(),
                        getSelectedDate(),
                        Observable.just(LocalTime.now()),
                        getDefaultNotifyTimeInMinutes(eventType),
                        (child, date, time, minutes) -> DiaperEvent.builder()
                                .child(child)
                                .dateTime(date.toDateTime(time))
                                .notifyTimeInMinutes(minutes)
                                .diaperState(DiaperState.WET)
                                .build());
            case FEED:
                return (Observable<T>) Observable.combineLatest(
                        childInteractor.getActiveChildOnce(),
                        getSelectedDate(),
                        Observable.just(LocalTime.now()),
                        getDefaultNotifyTimeInMinutes(eventType),
                        getDefaultFoodMeasure(),
                        (child, date, time, minutes, foodMeasure) -> FeedEvent.builder()
                                .child(child)
                                .dateTime(date.toDateTime(time))
                                .notifyTimeInMinutes(minutes)
                                .feedType(FeedType.BREAST_MILK)
                                .foodMeasure(foodMeasure)
                                .breast(Breast.LEFT)
                                .build());
            case OTHER:
                return (Observable<T>) Observable.combineLatest(
                        childInteractor.getActiveChildOnce(),
                        getSelectedDate(),
                        Observable.just(LocalTime.now()),
                        getDefaultNotifyTimeInMinutes(eventType),
                        (child, date, time, minutes) -> OtherEvent.builder()
                                .child(child)
                                .dateTime(date.toDateTime(time))
                                .notifyTimeInMinutes(minutes)
                                .build());
            case PUMP:
                return (Observable<T>) Observable.combineLatest(
                        childInteractor.getActiveChildOnce(),
                        getSelectedDate(),
                        Observable.just(LocalTime.now()),
                        getDefaultNotifyTimeInMinutes(eventType),
                        (child, date, time, minutes) -> PumpEvent.builder()
                                .child(child)
                                .dateTime(date.toDateTime(time))
                                .notifyTimeInMinutes(minutes)
                                .breast(Breast.LEFT)
                                .build());
            case SLEEP:
                return (Observable<T>) Observable.combineLatest(
                        childInteractor.getActiveChildOnce(),
                        getSelectedDate(),
                        Observable.just(LocalTime.now()),
                        getDefaultNotifyTimeInMinutes(eventType),
                        (child, date, time, minutes) -> SleepEvent.builder()
                                .child(child)
                                .dateTime(date.toDateTime(time))
                                .notifyTimeInMinutes(minutes)
                                .build());
        }
        throw new IllegalArgumentException("Unknown event type");
    }

    public Observable<EventsResponse> getAll(@NonNull EventsRequest request) {
        return calendarRepository.getAll(request.getChild(), request.getDate())
                .map(events -> EventsResponse.builder().request(request).events(events).build());
    }

    public Observable<List<SleepEvent>> getSleepEventsWithTimer() {
        return calendarRepository.getSleepEventsWithTimer();
    }

    @SuppressWarnings("unchecked")
    public <T extends MasterEvent> Observable<T> getEventDetail(@NonNull MasterEvent event) {
        if (event.getEventType() == EventType.DIAPER) {
            return (Observable<T>) calendarRepository.getDiaperEventDetail(event);
        } else if (event.getEventType() == EventType.FEED) {
            return (Observable<T>) calendarRepository.getFeedEventDetail(event);
        } else if (event.getEventType() == EventType.OTHER) {
            return (Observable<T>) calendarRepository.getOtherEventDetail(event);
        } else if (event.getEventType() == EventType.PUMP) {
            return (Observable<T>) calendarRepository.getPumpEventDetail(event);
        } else if (event.getEventType() == EventType.SLEEP) {
            return (Observable<T>) calendarRepository.getSleepEventDetail(event);
        }
        throw new IllegalArgumentException("Unknown event type");
    }

    @SuppressWarnings("unchecked")
    public <T extends MasterEvent> Observable<T> add(@NonNull AddEventRequest<T> request) {
        Child child = request.getChild();
        if (request.getEvent().getEventType() == EventType.DIAPER) {
            DiaperEvent event = (DiaperEvent) request.getEvent();
            event = event.toBuilder().child(child).build();
            return (Observable<T>) calendarRepository.add(event);
        } else if (request.getEvent().getEventType() == EventType.FEED) {
            FeedEvent event = (FeedEvent) request.getEvent();
            event = event.toBuilder().child(child).build();
            return (Observable<T>) calendarRepository.add(event);
        } else if (request.getEvent().getEventType() == EventType.OTHER) {
            OtherEvent event = (OtherEvent) request.getEvent();
            event = event.toBuilder().child(child).build();
            return (Observable<T>) calendarRepository.add(event);
        } else if (request.getEvent().getEventType() == EventType.PUMP) {
            PumpEvent event = (PumpEvent) request.getEvent();
            event = event.toBuilder().child(child).build();
            return (Observable<T>) calendarRepository.add(event);
        } else if (request.getEvent().getEventType() == EventType.SLEEP) {
            SleepEvent event = (SleepEvent) request.getEvent();
            event = event.toBuilder().child(child).build();
            return (Observable<T>) calendarRepository.add(event);
        }
        throw new IllegalArgumentException("Unknown event type");
    }

    @SuppressWarnings("unchecked")
    public <T extends MasterEvent> Observable<T> update(@NonNull T event) {
        if (event.getEventType() == EventType.DIAPER) {
            return (Observable<T>) calendarRepository.update((DiaperEvent) event);
        } else if (event.getEventType() == EventType.FEED) {
            return (Observable<T>) calendarRepository.update((FeedEvent) event);
        } else if (event.getEventType() == EventType.OTHER) {
            return (Observable<T>) calendarRepository.update((OtherEvent) event);
        } else if (event.getEventType() == EventType.PUMP) {
            return (Observable<T>) calendarRepository.update((PumpEvent) event);
        } else if (event.getEventType() == EventType.SLEEP) {
            return (Observable<T>) calendarRepository.update((SleepEvent) event);
        }
        throw new IllegalArgumentException("Unknown event type");
    }

    public Observable<MasterEvent> delete(@NonNull MasterEvent event) {
        return calendarRepository.delete(event);
    }

    public Observable<MasterEvent> done(@NonNull MasterEvent event) {
        return calendarRepository.done(event);
    }
}
