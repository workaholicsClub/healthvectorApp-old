package ru.android.childdiary.domain.interactors.calendar;

import android.content.Context;
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
import ru.android.childdiary.domain.core.Validator;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.calendar.requests.AddEventRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.EventsRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.EventsResponse;
import ru.android.childdiary.domain.interactors.calendar.validation.CalendarValidationException;
import ru.android.childdiary.domain.interactors.calendar.validation.CalendarValidationResult;
import ru.android.childdiary.domain.interactors.calendar.validation.DiaperEventValidator;
import ru.android.childdiary.domain.interactors.calendar.validation.FeedEventValidator;
import ru.android.childdiary.domain.interactors.calendar.validation.OtherEventValidator;
import ru.android.childdiary.domain.interactors.calendar.validation.PumpEventValidator;
import ru.android.childdiary.domain.interactors.calendar.validation.SleepEventValidator;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.utils.EventHelper;

public class CalendarInteractor implements Interactor {
    private final Logger logger = LoggerFactory.getLogger(toString());

    private final Context context;
    private final CalendarDataRepository calendarRepository;
    private final ChildInteractor childInteractor;

    @Inject
    public CalendarInteractor(Context context, CalendarDataRepository calendarRepository, ChildInteractor childInteractor) {
        this.context = context;
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

    public Observable<List<Food>> getFoodList() {
        return calendarRepository.getFoodList();
    }

    public Observable<Food> addFood(@NonNull Food food) {
        return calendarRepository.addFood(food);
    }

    private Observable<Food> getDefaultFood() {
        return calendarRepository.getFoodList()
                .first(Collections.singletonList(Food.NULL))
                .flatMapObservable(Observable::fromIterable)
                .first(Food.NULL)
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
        throw new IllegalStateException("Unknown event type");
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
        throw new IllegalStateException("Unknown event type");
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
        throw new IllegalStateException("Unknown event type");
    }

    public <T extends MasterEvent> Observable<T> add(@NonNull AddEventRequest<T> request) {
        return validate(preprocess(request.getEvent(), request.getChild()))
                .flatMap(event -> addInternal(event, request.getChild()));
    }

    @SuppressWarnings("unchecked")
    private <T extends MasterEvent> Observable<T> addInternal(@NonNull T event, @NonNull Child child) {
        if (event.getEventType() == EventType.DIAPER) {
            return (Observable<T>) calendarRepository.add((DiaperEvent) event);
        } else if (event.getEventType() == EventType.FEED) {
            return (Observable<T>) calendarRepository.add((FeedEvent) event);
        } else if (event.getEventType() == EventType.OTHER) {
            return (Observable<T>) calendarRepository.add((OtherEvent) event);
        } else if (event.getEventType() == EventType.PUMP) {
            return (Observable<T>) calendarRepository.add((PumpEvent) event);
        } else if (event.getEventType() == EventType.SLEEP) {
            return (Observable<T>) calendarRepository.add((SleepEvent) event);
        }
        throw new IllegalStateException("Unknown event type");
    }

    public <T extends MasterEvent> Observable<T> update(@NonNull T event) {
        return validate(preprocess(event, event.getChild())).flatMap(this::updateInternal);
    }

    @SuppressWarnings("unchecked")
    private <T extends MasterEvent> Observable<T> updateInternal(@NonNull T event) {
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
        throw new IllegalStateException("Unknown event type");
    }

    public Observable<MasterEvent> delete(@NonNull MasterEvent event) {
        return calendarRepository.delete(event);
    }

    public Observable<MasterEvent> done(@NonNull MasterEvent event) {
        return calendarRepository.done(event);
    }

    private <T extends MasterEvent> Observable<T> validate(@NonNull T item) {
        return Observable.just(item)
                .flatMap(event -> {
                    Validator<T, CalendarValidationResult> validator = getValidator(event);
                    List<CalendarValidationResult> results = validator.validate(event);
                    if (!validator.isValid(results)) {
                        return Observable.error(new CalendarValidationException(results));
                    }
                    return Observable.just(event);
                });
    }

    @SuppressWarnings("unchecked")
    private <T extends MasterEvent> Validator<T, CalendarValidationResult> getValidator(@NonNull T event) {
        if (event.getEventType() == EventType.DIAPER) {
            return (Validator<T, CalendarValidationResult>) new DiaperEventValidator(context);
        } else if (event.getEventType() == EventType.FEED) {
            return (Validator<T, CalendarValidationResult>) new FeedEventValidator(context);
        } else if (event.getEventType() == EventType.OTHER) {
            return (Validator<T, CalendarValidationResult>) new OtherEventValidator(context);
        } else if (event.getEventType() == EventType.PUMP) {
            return (Validator<T, CalendarValidationResult>) new PumpEventValidator(context);
        } else if (event.getEventType() == EventType.SLEEP) {
            return (Validator<T, CalendarValidationResult>) new SleepEventValidator(context, this);
        }
        throw new IllegalStateException("Unknown event type");
    }

    @SuppressWarnings("unchecked")
    private <T extends MasterEvent> T preprocess(@NonNull T event, @NonNull Child child) {
        if (event instanceof DiaperEvent) {
            DiaperEvent diaperEvent = (DiaperEvent) event;
            return (T) diaperEvent.toBuilder()
                    .eventType(EventType.DIAPER)
                    .description(EventHelper.getDescription(context, event))
                    .child(child)
                    .build();
        } else if (event instanceof FeedEvent) {
            FeedEvent feedEvent = (FeedEvent) event;
            return (T) feedEvent.toBuilder()
                    .eventType(EventType.FEED)
                    .description(EventHelper.getDescription(context, event))
                    .child(child)
                    .build();
        } else if (event instanceof OtherEvent) {
            OtherEvent otherEvent = (OtherEvent) event;
            return (T) otherEvent.toBuilder()
                    .eventType(EventType.OTHER)
                    .description(EventHelper.getDescription(context, event))
                    .child(child)
                    .build();
        } else if (event instanceof PumpEvent) {
            PumpEvent pumpEvent = (PumpEvent) event;
            return (T) pumpEvent.toBuilder()
                    .eventType(EventType.PUMP)
                    .description(EventHelper.getDescription(context, event))
                    .child(child)
                    .build();
        } else if (event instanceof SleepEvent) {
            SleepEvent sleepEvent = (SleepEvent) event;
            return (T) sleepEvent.toBuilder()
                    .eventType(EventType.SLEEP)
                    .description(EventHelper.getDescription(context, event))
                    .child(child)
                    .build();
        }
        throw new IllegalStateException("Unknown event type");
    }
}
