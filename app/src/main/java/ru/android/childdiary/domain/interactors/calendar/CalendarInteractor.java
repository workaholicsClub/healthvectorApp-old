package ru.android.childdiary.domain.interactors.calendar;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

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

    public Observable<List<Food>> getFoodList() {
        return calendarRepository.getFoodList();
    }

    public Observable<Food> addFood(@NonNull Food food) {
        return calendarRepository.addFood(food);
    }

    private Observable<Integer> getDefaultNotifyTimeInMinutes(@NonNull EventType eventType) {
        switch (eventType) {
            case DIAPER:
                return Observable.just(0);
            case FEED:
                return Observable.just(30);
            case OTHER:
                return Observable.just(10);
            case PUMP:
                return Observable.just(10);
            case SLEEP:
                return Observable.just(60);
        }
        throw new IllegalStateException("Unknown event type");
    }

    @SuppressWarnings("unchecked")
    public <T extends MasterEvent> Observable<T> getDefaultEventDetail(@NonNull EventType eventType) {
        switch (eventType) {
            case DIAPER:
                return (Observable<T>) getDefaultDiaperEvent();
            case FEED:
                return (Observable<T>) getDefaultFeedEvent();
            case OTHER:
                return (Observable<T>) getDefaultOtherEvent();
            case PUMP:
                return (Observable<T>) getDefaultPumpEvent();
            case SLEEP:
                return (Observable<T>) getDefaultSleepEvent();
        }
        throw new IllegalStateException("Unknown event type");
    }

    private Observable<DiaperEvent> getDefaultDiaperEvent() {
        return Observable.combineLatest(
                childInteractor.getActiveChildOnce(),
                getSelectedDate(),
                Observable.just(LocalTime.now()),
                getDefaultNotifyTimeInMinutes(EventType.DIAPER),
                (child, date, time, minutes) -> DiaperEvent.builder()
                        .child(child)
                        .dateTime(date.toDateTime(time))
                        .notifyTimeInMinutes(minutes)
                        .diaperState(DiaperState.WET)
                        .build());
    }

    private Observable<FeedEvent> getDefaultFeedEvent() {
        return Observable.combineLatest(
                childInteractor.getActiveChildOnce(),
                getSelectedDate(),
                Observable.just(LocalTime.now()),
                getDefaultNotifyTimeInMinutes(EventType.FEED),
                calendarRepository.getLastFeedType(),
                calendarRepository.getLastFoodMeasure(),
                calendarRepository.getLastFood(),
                (child, date, time, minutes, feedType, foodMeasure, food) -> FeedEvent.builder()
                        .child(child)
                        .dateTime(date.toDateTime(time))
                        .notifyTimeInMinutes(minutes)
                        .feedType(feedType)
                        .foodMeasure(foodMeasure)
                        .food(food)
                        .breast(Breast.LEFT)
                        .build());
    }

    private Observable<OtherEvent> getDefaultOtherEvent() {
        return Observable.combineLatest(
                childInteractor.getActiveChildOnce(),
                getSelectedDate(),
                Observable.just(LocalTime.now()),
                getDefaultNotifyTimeInMinutes(EventType.OTHER),
                (child, date, time, minutes) -> OtherEvent.builder()
                        .child(child)
                        .dateTime(date.toDateTime(time))
                        .notifyTimeInMinutes(minutes)
                        .build());
    }

    private Observable<PumpEvent> getDefaultPumpEvent() {
        return Observable.combineLatest(
                childInteractor.getActiveChildOnce(),
                getSelectedDate(),
                Observable.just(LocalTime.now()),
                getDefaultNotifyTimeInMinutes(EventType.PUMP),
                (child, date, time, minutes) -> PumpEvent.builder()
                        .child(child)
                        .dateTime(date.toDateTime(time))
                        .notifyTimeInMinutes(minutes)
                        .breast(Breast.LEFT)
                        .build());
    }

    private Observable<SleepEvent> getDefaultSleepEvent() {
        return Observable.combineLatest(
                childInteractor.getActiveChildOnce(),
                getSelectedDate(),
                Observable.just(LocalTime.now()),
                getDefaultNotifyTimeInMinutes(EventType.SLEEP),
                (child, date, time, minutes) -> SleepEvent.builder()
                        .child(child)
                        .dateTime(date.toDateTime(time))
                        .notifyTimeInMinutes(minutes)
                        .build());
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
        return validate(preprocessInsert(request.getChild(), request.getEvent()))
                .flatMap(this::addInternal)
                .flatMap(this::postprocess);
    }

    @SuppressWarnings("unchecked")
    private <T extends MasterEvent> Observable<T> addInternal(@NonNull T event) {
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
        return validate(preprocessUpdate(event))
                .flatMap(this::updateInternal)
                .flatMap(this::postprocess);
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

    public Observable<List<CalendarValidationResult>> controlFields(Observable<TextViewAfterTextChangeEvent> otherEventNameObservable) {
        return otherEventNameObservable
                .filter(textViewAfterTextChangeEvent -> textViewAfterTextChangeEvent.editable() != null)
                .map(textViewAfterTextChangeEvent -> textViewAfterTextChangeEvent.editable().toString())
                .map(otherEventName -> Collections.singletonList(new OtherEventValidator(context).validateOtherEventName(otherEventName)));
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
    private <T extends MasterEvent> T preprocessInsert(@NonNull Child child, @NonNull T event) {
        if (event instanceof DiaperEvent) {
            return (T) preprocessInsertDiaperEvent(child, (DiaperEvent) event);
        } else if (event instanceof FeedEvent) {
            return (T) preprocessInsertFeedEvent(child, (FeedEvent) event);
        } else if (event instanceof OtherEvent) {
            return (T) preprocessInsertOtherEvent(child, (OtherEvent) event);
        } else if (event instanceof PumpEvent) {
            return (T) preprocessInsertPumpEvent(child, (PumpEvent) event);
        } else if (event instanceof SleepEvent) {
            return (T) preprocessInsertSleepEvent(child, (SleepEvent) event);
        }
        throw new IllegalStateException("Unknown event type");
    }

    @SuppressWarnings("unchecked")
    private <T extends MasterEvent> T preprocessUpdate(@NonNull T event) {
        if (event instanceof DiaperEvent) {
            return (T) preprocessUpdateDiaperEvent((DiaperEvent) event);
        } else if (event instanceof FeedEvent) {
            return (T) preprocessUpdateFeedEvent((FeedEvent) event);
        } else if (event instanceof OtherEvent) {
            return (T) preprocessUpdateOtherEvent((OtherEvent) event);
        } else if (event instanceof PumpEvent) {
            return (T) preprocessUpdatePumpEvent((PumpEvent) event);
        } else if (event instanceof SleepEvent) {
            return (T) preprocessUpdateSleepEvent((SleepEvent) event);
        }
        throw new IllegalStateException("Unknown event type");
    }

    private DiaperEvent preprocessInsertDiaperEvent(@NonNull Child child, DiaperEvent diaperEvent) {
        return diaperEvent.toBuilder()
                .eventType(EventType.DIAPER)
                .child(child)
                .build();
    }

    private FeedEvent preprocessInsertFeedEvent(@NonNull Child child, FeedEvent feedEvent) {
        return feedEvent.toBuilder()
                .eventType(EventType.FEED)
                .child(child)
                .build();
    }

    private OtherEvent preprocessInsertOtherEvent(@NonNull Child child, OtherEvent otherEvent) {
        return otherEvent.toBuilder()
                .eventType(EventType.OTHER)
                .child(child)
                .build();
    }

    private PumpEvent preprocessInsertPumpEvent(@NonNull Child child, PumpEvent pumpEvent) {
        return pumpEvent.toBuilder()
                .eventType(EventType.PUMP)
                .child(child)
                .build();
    }

    private SleepEvent preprocessInsertSleepEvent(@NonNull Child child, SleepEvent sleepEvent) {
        return sleepEvent.toBuilder()
                .eventType(EventType.SLEEP)
                .child(child)
                .build();
    }

    private DiaperEvent preprocessUpdateDiaperEvent(DiaperEvent diaperEvent) {
        return diaperEvent.toBuilder()
                .eventType(EventType.DIAPER)
                .build();
    }

    private FeedEvent preprocessUpdateFeedEvent(FeedEvent feedEvent) {
        return feedEvent.toBuilder()
                .eventType(EventType.FEED)
                .build();
    }

    private OtherEvent preprocessUpdateOtherEvent(OtherEvent otherEvent) {
        return otherEvent.toBuilder()
                .eventType(EventType.OTHER)
                .build();
    }

    private PumpEvent preprocessUpdatePumpEvent(PumpEvent pumpEvent) {
        return pumpEvent.toBuilder()
                .eventType(EventType.PUMP)
                .build();
    }

    private SleepEvent preprocessUpdateSleepEvent(SleepEvent sleepEvent) {
        return sleepEvent.toBuilder()
                .eventType(EventType.SLEEP)
                .build();
    }

    @SuppressWarnings("unchecked")
    private <T extends MasterEvent> Observable<T> postprocess(@NonNull T event) {
        return Observable.fromCallable(() -> {
            if (event.getEventType() == EventType.DIAPER) {
                return event;
            } else if (event.getEventType() == EventType.FEED) {
                FeedEvent feedEvent = (FeedEvent) event;
                calendarRepository.setLastFeedType(feedEvent.getFeedType());
                calendarRepository.setLastFoodMeasure(feedEvent.getFoodMeasure());
                calendarRepository.setLastFood(feedEvent.getFood());
                return event;
            } else if (event.getEventType() == EventType.OTHER) {
                return event;
            } else if (event.getEventType() == EventType.PUMP) {
                return event;
            } else if (event.getEventType() == EventType.SLEEP) {
                return event;
            }
            throw new IllegalStateException("Unknown event type");
        });
    }
}
