package ru.android.childdiary.domain.interactors.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;

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
import ru.android.childdiary.data.repositories.child.ChildDataRepository;
import ru.android.childdiary.data.types.Breast;
import ru.android.childdiary.data.types.DiaperState;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.core.Interactor;
import ru.android.childdiary.domain.core.Validator;
import ru.android.childdiary.domain.interactors.calendar.events.core.Food;
import ru.android.childdiary.domain.interactors.calendar.events.core.FoodMeasure;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.calendar.requests.EventsRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.EventsResponse;
import ru.android.childdiary.domain.interactors.calendar.validation.CalendarValidationException;
import ru.android.childdiary.domain.interactors.calendar.validation.CalendarValidationResult;
import ru.android.childdiary.domain.interactors.calendar.validation.DiaperEventValidator;
import ru.android.childdiary.domain.interactors.calendar.validation.FeedEventValidator;
import ru.android.childdiary.domain.interactors.calendar.validation.OtherEventValidator;
import ru.android.childdiary.domain.interactors.calendar.validation.PumpEventValidator;
import ru.android.childdiary.domain.interactors.calendar.validation.SleepEventValidator;
import ru.android.childdiary.domain.interactors.child.ChildRepository;
import ru.android.childdiary.domain.interactors.core.PeriodicityType;

public class CalendarInteractor implements Interactor {
    private final Logger logger = LoggerFactory.getLogger(toString());

    private final Context context;
    private final ChildRepository childRepository;
    private final CalendarRepository calendarRepository;

    @Inject
    public CalendarInteractor(Context context,
                              ChildDataRepository childRepository,
                              CalendarDataRepository calendarRepository) {
        this.context = context;
        this.childRepository = childRepository;
        this.calendarRepository = calendarRepository;
    }

    public Observable<LocalDate> getSelectedDate() {
        return calendarRepository.getSelectedDate();
    }

    public void setSelectedDate(@NonNull LocalDate date) {
        calendarRepository.setSelectedDate(date);
    }

    public Observable<LocalDate> getSelectedDateOnce() {
        return calendarRepository.getSelectedDateOnce();
    }

    public Observable<LocalDate> setSelectedDateObservable(@NonNull LocalDate date) {
        return calendarRepository.setSelectedDateObservable(date);
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

    public Observable<DiaperEvent> getDefaultDiaperEvent() {
        return Observable.combineLatest(
                childRepository.getActiveChildOnce(),
                getSelectedDateOnce(),
                Observable.just(LocalTime.now()),
                calendarRepository.getDefaultNotifyTimeInMinutes(EventType.DIAPER),
                (child, date, time, minutes) -> DiaperEvent.builder()
                        .child(child)
                        .dateTime(date.toDateTime(time).withSecondOfMinute(0).withMillisOfSecond(0))
                        .notifyTimeInMinutes(minutes)
                        .diaperState(DiaperState.WET)
                        .build());
    }

    public Observable<FeedEvent> getDefaultFeedEvent() {
        return Observable.combineLatest(
                childRepository.getActiveChildOnce(),
                getSelectedDateOnce(),
                Observable.just(LocalTime.now()),
                calendarRepository.getDefaultNotifyTimeInMinutes(EventType.FEED),
                calendarRepository.getLastFeedType(),
                calendarRepository.getLastFoodMeasure(),
                calendarRepository.getLastFood(),
                (child, date, time, minutes, feedType, foodMeasure, food) -> FeedEvent.builder()
                        .child(child)
                        .dateTime(date.toDateTime(time).withSecondOfMinute(0).withMillisOfSecond(0))
                        .notifyTimeInMinutes(minutes)
                        .feedType(feedType)
                        .foodMeasure(foodMeasure)
                        .food(food)
                        .breast(Breast.LEFT)
                        .build());
    }

    public Observable<OtherEvent> getDefaultOtherEvent() {
        return Observable.combineLatest(
                childRepository.getActiveChildOnce(),
                getSelectedDateOnce(),
                Observable.just(LocalTime.now()),
                calendarRepository.getDefaultNotifyTimeInMinutes(EventType.OTHER),
                (child, date, time, minutes) -> OtherEvent.builder()
                        .child(child)
                        .dateTime(date.toDateTime(time).withSecondOfMinute(0).withMillisOfSecond(0))
                        .notifyTimeInMinutes(minutes)
                        .build());
    }

    public Observable<PumpEvent> getDefaultPumpEvent() {
        return Observable.combineLatest(
                childRepository.getActiveChildOnce(),
                getSelectedDateOnce(),
                Observable.just(LocalTime.now()),
                calendarRepository.getDefaultNotifyTimeInMinutes(EventType.PUMP),
                (child, date, time, minutes) -> PumpEvent.builder()
                        .child(child)
                        .dateTime(date.toDateTime(time).withSecondOfMinute(0).withMillisOfSecond(0))
                        .notifyTimeInMinutes(minutes)
                        .breast(Breast.LEFT)
                        .build());
    }

    public Observable<SleepEvent> getDefaultSleepEvent() {
        return Observable.combineLatest(
                childRepository.getActiveChildOnce(),
                getSelectedDateOnce(),
                Observable.just(LocalTime.now()),
                calendarRepository.getDefaultNotifyTimeInMinutes(EventType.SLEEP),
                (child, date, time, minutes) -> SleepEvent.builder()
                        .child(child)
                        .dateTime(date.toDateTime(time).withSecondOfMinute(0).withMillisOfSecond(0))
                        .notifyTimeInMinutes(minutes)
                        .build());
    }

    public Observable<EventsResponse> getAll(@NonNull EventsRequest request) {
        return calendarRepository.getAll(request)
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

    public <T extends MasterEvent> Observable<T> add(@NonNull T event) {
        return preprocessOnInsert(event)
                .flatMap(this::validate)
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
        return preprocessOnUpdate(event)
                .flatMap(this::validate)
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

    public Observable<Boolean> controlOtherEventDoneButton(@NonNull Observable<TextViewAfterTextChangeEvent> otherEventNameObservable) {
        return otherEventNameObservable
                .map(otherEventNameAfterTextChangeEvent -> otherEventNameAfterTextChangeEvent.editable() != null
                        && !TextUtils.isEmpty(otherEventNameAfterTextChangeEvent.editable()))
                .distinctUntilChanged();
    }

    public Observable<List<CalendarValidationResult>> controlOtherEventFields(@NonNull Observable<TextViewAfterTextChangeEvent> otherEventNameObservable) {
        return otherEventNameObservable
                .filter(otherEventNameAfterTextChangeEvent -> otherEventNameAfterTextChangeEvent.editable() != null)
                .map(TextViewAfterTextChangeEvent::editable)
                .map(Editable::toString)
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
    private <T extends MasterEvent> Observable<T> preprocessOnInsert(@NonNull T event) {
        return Observable.fromCallable(() -> {
            if (event instanceof DiaperEvent) {
                return (T) preprocessDiaperEventOnInsert((DiaperEvent) event);
            } else if (event instanceof FeedEvent) {
                return (T) preprocessFeedEventOnInsert((FeedEvent) event);
            } else if (event instanceof OtherEvent) {
                return (T) preprocessOtherEventOnInsert((OtherEvent) event);
            } else if (event instanceof PumpEvent) {
                return (T) preprocessPumpEventOnInsert((PumpEvent) event);
            } else if (event instanceof SleepEvent) {
                return (T) preprocessSleepEventOnInsert((SleepEvent) event);
            }
            throw new IllegalStateException("Unknown event type");
        });
    }

    @SuppressWarnings("unchecked")
    private <T extends MasterEvent> Observable<T> preprocessOnUpdate(@NonNull T event) {
        return Observable.fromCallable(() -> {
            if (event instanceof DiaperEvent) {
                return (T) preprocessDiaperEventOnUpdate((DiaperEvent) event);
            } else if (event instanceof FeedEvent) {
                return (T) preprocessFeedEventOnUpdate((FeedEvent) event);
            } else if (event instanceof OtherEvent) {
                return (T) preprocessOtherEventOnUpdate((OtherEvent) event);
            } else if (event instanceof PumpEvent) {
                return (T) preprocessPumpEventOnUpdate((PumpEvent) event);
            } else if (event instanceof SleepEvent) {
                return (T) preprocessSleepEventOnUpdate((SleepEvent) event);
            }
            throw new IllegalStateException("Unknown event type");
        });
    }

    private DiaperEvent preprocessDiaperEventOnInsert(DiaperEvent diaperEvent) {
        return diaperEvent.toBuilder()
                .id(null)
                .masterEventId(null)
                .eventType(EventType.DIAPER)
                .build();
    }

    private FeedEvent preprocessFeedEventOnInsert(FeedEvent feedEvent) {
        return feedEvent.toBuilder()
                .id(null)
                .masterEventId(null)
                .eventType(EventType.FEED)
                .build();
    }

    private OtherEvent preprocessOtherEventOnInsert(OtherEvent otherEvent) {
        return otherEvent.toBuilder()
                .id(null)
                .masterEventId(null)
                .eventType(EventType.OTHER)
                .build();
    }

    private PumpEvent preprocessPumpEventOnInsert(PumpEvent pumpEvent) {
        return pumpEvent.toBuilder()
                .id(null)
                .masterEventId(null)
                .eventType(EventType.PUMP)
                .build();
    }

    private SleepEvent preprocessSleepEventOnInsert(SleepEvent sleepEvent) {
        return sleepEvent.toBuilder()
                .id(null)
                .masterEventId(null)
                .eventType(EventType.SLEEP)
                .build();
    }

    private DiaperEvent preprocessDiaperEventOnUpdate(DiaperEvent diaperEvent) {
        return diaperEvent.toBuilder()
                .eventType(EventType.DIAPER)
                .build();
    }

    private FeedEvent preprocessFeedEventOnUpdate(FeedEvent feedEvent) {
        return feedEvent.toBuilder()
                .eventType(EventType.FEED)
                .build();
    }

    private OtherEvent preprocessOtherEventOnUpdate(OtherEvent otherEvent) {
        return otherEvent.toBuilder()
                .eventType(EventType.OTHER)
                .build();
    }

    private PumpEvent preprocessPumpEventOnUpdate(PumpEvent pumpEvent) {
        return pumpEvent.toBuilder()
                .eventType(EventType.PUMP)
                .build();
    }

    private SleepEvent preprocessSleepEventOnUpdate(SleepEvent sleepEvent) {
        return sleepEvent.toBuilder()
                .eventType(EventType.SLEEP)
                .build();
    }

    @SuppressWarnings("unchecked")
    private <T extends MasterEvent> Observable<T> postprocess(@NonNull T event) {
        return Observable.fromCallable(() -> {
            calendarRepository.setSelectedDate(event.getDateTime().toLocalDate());
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

    public Observable<List<Integer>> getFrequencyList() {
        return calendarRepository.getFrequencyList();
    }

    public Observable<List<PeriodicityType>> getPeriodicityList() {
        return calendarRepository.getPeriodicityList();
    }
}
