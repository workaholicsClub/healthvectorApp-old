package ru.android.childdiary.domain.interactors.calendar;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.calendar.CalendarDataRepository;
import ru.android.childdiary.data.types.EventType;
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

public class CalendarInteractor implements Interactor {
    private final Logger logger = LoggerFactory.getLogger(toString());

    private final CalendarDataRepository calendarRepository;

    @Inject
    public CalendarInteractor(CalendarDataRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
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

    public Observable<FoodMeasure> getDefaultFoodMeasure() {
        return calendarRepository.getFoodMeasureList()
                .firstOrError()
                .flatMapObservable(Observable::fromIterable)
                .first(FoodMeasure.NULL)
                .toObservable();
    }

    public Observable<Integer> getDefaultNotifyTimeInMinutes(@NonNull EventType eventType) {
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
        return null;
    }

    public Observable<EventsResponse> getAll(@NonNull EventsRequest request) {
        return calendarRepository.getAll(request.getChild(), request.getDate())
                .map(events -> EventsResponse.builder().request(request).events(events).build());
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
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends MasterEvent> Observable<T> add(@NonNull AddEventRequest<T> request) {
        if (request.getEvent().getEventType() == EventType.DIAPER) {
            return (Observable<T>) calendarRepository.add(request.getChild(), (DiaperEvent) request.getEvent());
        } else if (request.getEvent().getEventType() == EventType.FEED) {
            return (Observable<T>) calendarRepository.add(request.getChild(), (FeedEvent) request.getEvent());
        } else if (request.getEvent().getEventType() == EventType.OTHER) {
            return (Observable<T>) calendarRepository.add(request.getChild(), (OtherEvent) request.getEvent());
        } else if (request.getEvent().getEventType() == EventType.PUMP) {
            return (Observable<T>) calendarRepository.add(request.getChild(), (PumpEvent) request.getEvent());
        } else if (request.getEvent().getEventType() == EventType.SLEEP) {
            return (Observable<T>) calendarRepository.add(request.getChild(), (SleepEvent) request.getEvent());
        }
        return null;
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
        return null;
    }

    public Observable<MasterEvent> delete(@NonNull MasterEvent event) {
        return calendarRepository.delete(event);
    }

    public Observable<MasterEvent> done(@NonNull MasterEvent event) {
        return calendarRepository.done(event);
    }
}
