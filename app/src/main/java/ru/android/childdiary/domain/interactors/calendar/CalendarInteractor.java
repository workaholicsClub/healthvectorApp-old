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

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.calendar.CalendarDataRepository;
import ru.android.childdiary.data.repositories.child.ChildDataRepository;
import ru.android.childdiary.data.repositories.core.images.ImagesDataRepository;
import ru.android.childdiary.data.types.Breast;
import ru.android.childdiary.data.types.DiaperState;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.core.DeleteResponse;
import ru.android.childdiary.domain.core.validation.Validator;
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
import ru.android.childdiary.domain.interactors.calendar.requests.GetEventsRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.GetEventsResponse;
import ru.android.childdiary.domain.interactors.calendar.requests.GetSleepEventsRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.GetSleepEventsResponse;
import ru.android.childdiary.domain.interactors.calendar.requests.UpdateDoctorVisitEventRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.UpdateDoctorVisitEventResponse;
import ru.android.childdiary.domain.interactors.calendar.requests.UpdateMedicineTakingEventRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.UpdateMedicineTakingEventResponse;
import ru.android.childdiary.domain.interactors.calendar.validation.CalendarValidationException;
import ru.android.childdiary.domain.interactors.calendar.validation.CalendarValidationResult;
import ru.android.childdiary.domain.interactors.calendar.validation.DiaperEventValidator;
import ru.android.childdiary.domain.interactors.calendar.validation.DoctorVisitEventValidator;
import ru.android.childdiary.domain.interactors.calendar.validation.FeedEventValidator;
import ru.android.childdiary.domain.interactors.calendar.validation.MedicineTakingEventValidator;
import ru.android.childdiary.domain.interactors.calendar.validation.OtherEventValidator;
import ru.android.childdiary.domain.interactors.calendar.validation.PumpEventValidator;
import ru.android.childdiary.domain.interactors.calendar.validation.SleepEventValidator;
import ru.android.childdiary.domain.interactors.child.ChildRepository;
import ru.android.childdiary.domain.interactors.core.PeriodicityType;
import ru.android.childdiary.domain.interactors.core.TimeUnit;
import ru.android.childdiary.domain.interactors.core.images.ImageType;
import ru.android.childdiary.domain.interactors.core.images.ImagesRepository;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteDoctorVisitEventsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteDoctorVisitEventsResponse;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteMedicineTakingEventsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteMedicineTakingEventsResponse;

public class CalendarInteractor {
    private final Logger logger = LoggerFactory.getLogger(toString());

    private final Context context;
    private final ChildRepository childRepository;
    private final CalendarRepository calendarRepository;
    private final DiaperEventValidator diaperEventValidator;
    private final FeedEventValidator feedEventValidator;
    private final OtherEventValidator otherEventValidator;
    private final PumpEventValidator pumpEventValidator;
    private final SleepEventValidator sleepEventValidator;
    private final DoctorVisitEventValidator doctorVisitEventValidator;
    private final MedicineTakingEventValidator medicineTakingEventValidator;
    private final ImagesRepository imagesRepository;

    @Inject
    public CalendarInteractor(Context context,
                              ChildDataRepository childRepository,
                              CalendarDataRepository calendarRepository,
                              DiaperEventValidator diaperEventValidator,
                              FeedEventValidator feedEventValidator,
                              OtherEventValidator otherEventValidator,
                              PumpEventValidator pumpEventValidator,
                              SleepEventValidator sleepEventValidator,
                              DoctorVisitEventValidator doctorVisitEventValidator,
                              MedicineTakingEventValidator medicineTakingEventValidator,
                              ImagesDataRepository imagesRepository) {
        this.context = context;
        this.childRepository = childRepository;
        this.calendarRepository = calendarRepository;
        this.diaperEventValidator = diaperEventValidator;
        this.feedEventValidator = feedEventValidator;
        this.otherEventValidator = otherEventValidator;
        this.pumpEventValidator = pumpEventValidator;
        this.sleepEventValidator = sleepEventValidator;
        this.doctorVisitEventValidator = doctorVisitEventValidator;
        this.medicineTakingEventValidator = medicineTakingEventValidator;
        this.imagesRepository = imagesRepository;
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

    public Observable<DoctorVisitEvent> getDefaultDoctorVisitEvent() {
        return Observable.combineLatest(
                childRepository.getActiveChildOnce(),
                getSelectedDateOnce(),
                Observable.just(LocalTime.now()),
                calendarRepository.getDefaultNotifyTimeInMinutes(EventType.DOCTOR_VISIT),
                (child, date, time, minutes) -> DoctorVisitEvent.builder()
                        .child(child)
                        .dateTime(date.toDateTime(time).withSecondOfMinute(0).withMillisOfSecond(0))
                        .notifyTimeInMinutes(minutes)
                        .build());
    }

    public Observable<MedicineTakingEvent> getDefaultMedicineTakingEvent() {
        return Observable.combineLatest(
                childRepository.getActiveChildOnce(),
                getSelectedDateOnce(),
                Observable.just(LocalTime.now()),
                calendarRepository.getDefaultNotifyTimeInMinutes(EventType.MEDICINE_TAKING),
                (child, date, time, minutes) -> MedicineTakingEvent.builder()
                        .child(child)
                        .dateTime(date.toDateTime(time).withSecondOfMinute(0).withMillisOfSecond(0))
                        .notifyTimeInMinutes(minutes)
                        .build());
    }

    public Observable<GetEventsResponse> getAll(@NonNull GetEventsRequest request) {
        return calendarRepository.getAll(request);
    }

    public Observable<GetSleepEventsResponse> getSleepEvents(@NonNull GetSleepEventsRequest request) {
        return calendarRepository.getSleepEvents(request);
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
        } else if (event.getEventType() == EventType.DOCTOR_VISIT) {
            return (Observable<T>) calendarRepository.getDoctorVisitEventDetail(event);
        } else if (event.getEventType() == EventType.MEDICINE_TAKING) {
            return (Observable<T>) calendarRepository.getMedicineTakingEventDetail(event);
        }
        // TODO EXERCISE
        throw new IllegalStateException("Unsupported event type");
    }

    @SuppressWarnings("unchecked")
    public <T extends MasterEvent> Observable<T> getEventDetailOnce(@NonNull MasterEvent event) {
        return (Observable<T>) getEventDetail(event).firstOrError().toObservable();
    }

    public <T extends MasterEvent> Observable<T> add(@NonNull T event) {
        return preprocessOnInsert(event)
                .flatMap(this::validate)
                .flatMap(this::addInternal)
                .flatMap(this::postprocessOnInsert);
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
        throw new IllegalStateException("Unsupported event type");
    }

    public <T extends MasterEvent> Observable<T> update(@NonNull T event) {
        return preprocessOnUpdate(event)
                .flatMap(this::validate)
                .flatMap(this::createImageFile)
                .flatMap(this::updateInternal)
                .flatMap(this::postprocessOnUpdate);
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
        } else if (event.getEventType() == EventType.DOCTOR_VISIT) {
            return (Observable<T>) calendarRepository.update(UpdateDoctorVisitEventRequest.builder()
                    .doctorVisitEvent((DoctorVisitEvent) event)
                    .build())
                    .flatMap(this::deleteImageFiles)
                    .map(UpdateDoctorVisitEventResponse::getDoctorVisitEvent);
        } else if (event.getEventType() == EventType.MEDICINE_TAKING) {
            return (Observable<T>) calendarRepository.update(UpdateMedicineTakingEventRequest.builder()
                    .medicineTakingEvent((MedicineTakingEvent) event)
                    .build())
                    .flatMap(this::deleteImageFiles)
                    .map(UpdateMedicineTakingEventResponse::getMedicineTakingEvent);
        }
        // TODO EXERCISE
        throw new IllegalStateException("Unsupported event type");
    }

    public <T extends MasterEvent> Observable<T> delete(@NonNull T event) {
        return calendarRepository.delete(event)
                .flatMap(imageFilesToDelete -> deleteImageFiles(event, imageFilesToDelete));
    }

    private <T extends MasterEvent> Observable<T> deleteImageFiles(@NonNull T event, List<String> imageFilesToDelete) {
        return Observable.fromCallable(() -> {
            imagesRepository.deleteImageFiles(imageFilesToDelete);
            return event;
        });
    }

    private <T extends DeleteResponse> Observable<T> deleteImageFiles(@NonNull T response) {
        return Observable.fromCallable(() -> {
            imagesRepository.deleteImageFiles(response.getImageFilesToDelete());
            return response;
        });
    }

    public Observable<MasterEvent> done(@NonNull MasterEvent event) {
        return calendarRepository.done(event);
    }

    public <T extends MasterEvent> Observable<Integer> deleteLinearGroup(@NonNull T event) {
        if (event.getEventType() == EventType.DIAPER) {
            return Observable.error(new IllegalArgumentException("Unsupported event type"));
        } else if (event.getEventType() == EventType.FEED) {
            return Observable.error(new IllegalArgumentException("Unsupported event type"));
        } else if (event.getEventType() == EventType.OTHER) {
            return Observable.error(new IllegalArgumentException("Unsupported event type"));
        } else if (event.getEventType() == EventType.PUMP) {
            return Observable.error(new IllegalArgumentException("Unsupported event type"));
        } else if (event.getEventType() == EventType.SLEEP) {
            return Observable.error(new IllegalArgumentException("Unsupported event type"));
        } else if (event.getEventType() == EventType.DOCTOR_VISIT) {
            DoctorVisitEvent doctorVisitEvent = (DoctorVisitEvent) event;
            DoctorVisit doctorVisit = doctorVisitEvent.getDoctorVisit();
            Integer linearGroup = doctorVisitEvent.getLinearGroup();
            return calendarRepository.deleteLinearGroup(DeleteDoctorVisitEventsRequest.builder()
                    .doctorVisit(doctorVisit)
                    .linearGroup(linearGroup)
                    .build())
                    .flatMap(this::deleteImageFiles)
                    .map(DeleteDoctorVisitEventsResponse::getCount);
        } else if (event.getEventType() == EventType.MEDICINE_TAKING) {
            MedicineTakingEvent medicineTakingEvent = (MedicineTakingEvent) event;
            MedicineTaking medicineTaking = medicineTakingEvent.getMedicineTaking();
            Integer linearGroup = medicineTakingEvent.getLinearGroup();
            return calendarRepository.deleteLinearGroup(DeleteMedicineTakingEventsRequest.builder()
                    .medicineTaking(medicineTaking)
                    .linearGroup(linearGroup)
                    .build())
                    .flatMap(this::deleteImageFiles)
                    .map(DeleteMedicineTakingEventsResponse::getCount);
        }
        // TODO EXERCISE
        throw new IllegalStateException("Unsupported event type");
    }

    public Observable<Boolean> controlOtherEventDoneButton(@NonNull Observable<TextViewAfterTextChangeEvent> otherEventNameObservable) {
        return otherEventNameObservable
                .map(TextViewAfterTextChangeEvent::editable)
                .map(Editable::toString)
                .map(otherEventName -> !TextUtils.isEmpty(otherEventName))
                .distinctUntilChanged();
    }

    public Observable<List<CalendarValidationResult>> controlOtherEventFields(@NonNull Observable<TextViewAfterTextChangeEvent> otherEventNameObservable) {
        return otherEventNameObservable
                .map(TextViewAfterTextChangeEvent::editable)
                .map(Editable::toString)
                .map(otherEventName -> OtherEvent.builder().name(otherEventName).build())
                .map(otherEventValidator::validate);
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

    private <T extends MasterEvent> Observable<T> createImageFile(@NonNull T item) {
        return Observable.fromCallable(() -> {
            if (item.getEventType() == EventType.DOCTOR_VISIT) {
                DoctorVisitEvent doctorVisitEvent = (DoctorVisitEvent) item;
                if (imagesRepository.isTemporaryImageFile(doctorVisitEvent.getImageFileName())) {
                    String uniqueImageFileName = imagesRepository.createUniqueImageFileRelativePath(
                            ImageType.DOCTOR_VISIT_EVENT, doctorVisitEvent.getImageFileName());
                    //noinspection unchecked
                    return (T) doctorVisitEvent.toBuilder().imageFileName(uniqueImageFileName).build();
                }
            } else if (item.getEventType() == EventType.MEDICINE_TAKING) {
                MedicineTakingEvent medicineTakingEvent = (MedicineTakingEvent) item;
                if (imagesRepository.isTemporaryImageFile(medicineTakingEvent.getImageFileName())) {
                    String uniqueImageFileName = imagesRepository.createUniqueImageFileRelativePath(
                            ImageType.MEDICINE_TAKING_EVENT, medicineTakingEvent.getImageFileName());
                    //noinspection unchecked
                    return (T) medicineTakingEvent.toBuilder().imageFileName(uniqueImageFileName).build();
                }
            }
            return item;
        });
    }

    @SuppressWarnings("unchecked")
    private <T extends MasterEvent> Validator<T, CalendarValidationResult> getValidator(@NonNull T event) {
        if (event.getEventType() == EventType.DIAPER) {
            return (Validator<T, CalendarValidationResult>) diaperEventValidator;
        } else if (event.getEventType() == EventType.FEED) {
            return (Validator<T, CalendarValidationResult>) feedEventValidator;
        } else if (event.getEventType() == EventType.OTHER) {
            return (Validator<T, CalendarValidationResult>) otherEventValidator;
        } else if (event.getEventType() == EventType.PUMP) {
            return (Validator<T, CalendarValidationResult>) pumpEventValidator;
        } else if (event.getEventType() == EventType.SLEEP) {
            return (Validator<T, CalendarValidationResult>) sleepEventValidator;
        } else if (event.getEventType() == EventType.DOCTOR_VISIT) {
            return (Validator<T, CalendarValidationResult>) doctorVisitEventValidator;
        } else if (event.getEventType() == EventType.MEDICINE_TAKING) {
            return (Validator<T, CalendarValidationResult>) medicineTakingEventValidator;
        }
        // TODO EXERCISE
        throw new IllegalStateException("Unsupported event type");
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
            throw new IllegalStateException("Unsupported event type");
        });
    }

    private <T extends MasterEvent> Observable<T> preprocessOnUpdate(@NonNull T event) {
        return Observable.fromCallable(() -> {
            if (event.getEventType() == EventType.DIAPER) {
                return event;
            } else if (event.getEventType() == EventType.FEED) {
                return event;
            } else if (event.getEventType() == EventType.OTHER) {
                return event;
            } else if (event.getEventType() == EventType.PUMP) {
                return event;
            } else if (event.getEventType() == EventType.SLEEP) {
                return event;
            } else if (event.getEventType() == EventType.DOCTOR_VISIT) {
                return event;
            } else if (event.getEventType() == EventType.MEDICINE_TAKING) {
                return event;
            }
            // TODO EXERCISE
            throw new IllegalStateException("Unsupported event type");
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

    @SuppressWarnings("unchecked")
    private <T extends MasterEvent> Observable<T> postprocessOnInsert(@NonNull T event) {
        return Observable.fromCallable(() -> {
            calendarRepository.setSelectedDate(event.getDateTime().toLocalDate());
            if (event.getEventType() == EventType.DIAPER) {
                return event;
            } else if (event.getEventType() == EventType.FEED) {
                return (T) postprocessFeedEventOnUpsert((FeedEvent) event);
            } else if (event.getEventType() == EventType.OTHER) {
                return event;
            } else if (event.getEventType() == EventType.PUMP) {
                return event;
            } else if (event.getEventType() == EventType.SLEEP) {
                return event;
            }
            throw new IllegalStateException("Unsupported event type");
        });
    }

    @SuppressWarnings("unchecked")
    private <T extends MasterEvent> Observable<T> postprocessOnUpdate(@NonNull T event) {
        return Observable.fromCallable(() -> {
            calendarRepository.setSelectedDate(event.getDateTime().toLocalDate());
            if (event.getEventType() == EventType.DIAPER) {
                return event;
            } else if (event.getEventType() == EventType.FEED) {
                return (T) postprocessFeedEventOnUpsert((FeedEvent) event);
            } else if (event.getEventType() == EventType.OTHER) {
                return event;
            } else if (event.getEventType() == EventType.PUMP) {
                return event;
            } else if (event.getEventType() == EventType.SLEEP) {
                return event;
            } else if (event.getEventType() == EventType.DOCTOR_VISIT) {
                return event;
            } else if (event.getEventType() == EventType.MEDICINE_TAKING) {
                return event;
            }
            // TODO EXERCISE
            throw new IllegalStateException("Unsupported event type");
        });
    }

    private FeedEvent postprocessFeedEventOnUpsert(FeedEvent feedEvent) {
        calendarRepository.setLastFeedType(feedEvent.getFeedType());
        calendarRepository.setLastFoodMeasure(feedEvent.getFoodMeasure());
        calendarRepository.setLastFood(feedEvent.getFood());
        return feedEvent;
    }

    public Observable<List<Integer>> getFrequencyList() {
        return calendarRepository.getFrequencyList();
    }

    public Observable<List<PeriodicityType>> getPeriodicityList() {
        return calendarRepository.getPeriodicityList();
    }

    public Observable<List<TimeUnit>> getTimeUnits() {
        return calendarRepository.getTimeUnits();
    }
}
