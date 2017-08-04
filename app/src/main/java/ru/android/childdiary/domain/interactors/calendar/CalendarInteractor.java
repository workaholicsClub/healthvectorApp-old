package ru.android.childdiary.domain.interactors.calendar;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;

import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.calendar.CalendarDataRepository;
import ru.android.childdiary.data.repositories.calendar.CalendarFilterDataRepository;
import ru.android.childdiary.data.repositories.child.ChildDataRepository;
import ru.android.childdiary.data.repositories.core.images.ImagesDataRepository;
import ru.android.childdiary.data.types.Breast;
import ru.android.childdiary.data.types.DiaperState;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.data.DoctorVisitEvent;
import ru.android.childdiary.domain.interactors.calendar.data.ExerciseEvent;
import ru.android.childdiary.domain.interactors.calendar.data.MedicineTakingEvent;
import ru.android.childdiary.domain.interactors.calendar.data.core.LinearGroupFieldType;
import ru.android.childdiary.domain.interactors.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.data.core.PeriodicityType;
import ru.android.childdiary.domain.interactors.calendar.data.core.TimeUnit;
import ru.android.childdiary.domain.interactors.calendar.data.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.data.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.data.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.data.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.data.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.calendar.requests.GetEventsFilter;
import ru.android.childdiary.domain.interactors.calendar.requests.GetEventsRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.GetEventsResponse;
import ru.android.childdiary.domain.interactors.calendar.requests.GetSleepEventsRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.GetSleepEventsResponse;
import ru.android.childdiary.domain.interactors.calendar.requests.UpdateDoctorVisitEventRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.UpdateDoctorVisitEventResponse;
import ru.android.childdiary.domain.interactors.calendar.requests.UpdateExerciseEventRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.UpdateExerciseEventResponse;
import ru.android.childdiary.domain.interactors.calendar.requests.UpdateMedicineTakingEventRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.UpdateMedicineTakingEventResponse;
import ru.android.childdiary.domain.interactors.calendar.validation.CalendarValidationResult;
import ru.android.childdiary.domain.interactors.calendar.validation.DiaperEventValidator;
import ru.android.childdiary.domain.interactors.calendar.validation.DoctorVisitEventValidator;
import ru.android.childdiary.domain.interactors.calendar.validation.ExerciseEventValidator;
import ru.android.childdiary.domain.interactors.calendar.validation.FeedEventValidator;
import ru.android.childdiary.domain.interactors.calendar.validation.MedicineTakingEventValidator;
import ru.android.childdiary.domain.interactors.calendar.validation.OtherEventValidator;
import ru.android.childdiary.domain.interactors.calendar.validation.PumpEventValidator;
import ru.android.childdiary.domain.interactors.calendar.validation.SleepEventValidator;
import ru.android.childdiary.domain.interactors.child.ChildRepository;
import ru.android.childdiary.domain.interactors.core.ValueRepository;
import ru.android.childdiary.domain.interactors.core.images.ImageType;
import ru.android.childdiary.domain.interactors.core.images.ImagesRepository;
import ru.android.childdiary.domain.interactors.core.requests.DeleteResponse;
import ru.android.childdiary.domain.interactors.core.validation.core.Validator;
import ru.android.childdiary.domain.interactors.exercises.data.ConcreteExercise;
import ru.android.childdiary.domain.interactors.exercises.requests.DeleteConcreteExerciseEventsRequest;
import ru.android.childdiary.domain.interactors.exercises.requests.DeleteConcreteExerciseEventsResponse;
import ru.android.childdiary.domain.interactors.medical.data.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.data.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteDoctorVisitEventsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteDoctorVisitEventsResponse;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteMedicineTakingEventsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteMedicineTakingEventsResponse;
import ru.android.childdiary.services.TimerService;
import ru.android.childdiary.utils.strings.EventUtils;

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
    private final ExerciseEventValidator exerciseEventValidator;
    private final ImagesRepository imagesRepository;
    private final ValueRepository<GetEventsFilter> filterRepository;

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
                              ExerciseEventValidator exerciseEventValidator,
                              ImagesDataRepository imagesRepository,
                              CalendarFilterDataRepository filterRepository) {
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
        this.exerciseEventValidator = exerciseEventValidator;
        this.imagesRepository = imagesRepository;
        this.filterRepository = filterRepository;
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

    public Observable<ExerciseEvent> getDefaultExerciseEvent() {
        return Observable.combineLatest(
                childRepository.getActiveChildOnce(),
                getSelectedDateOnce(),
                Observable.just(LocalTime.now()),
                calendarRepository.getDefaultNotifyTimeInMinutes(EventType.EXERCISE),
                (child, date, time, minutes) -> ExerciseEvent.builder()
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
        } else if (event.getEventType() == EventType.EXERCISE) {
            return (Observable<T>) calendarRepository.getExerciseEventDetail(event);
        }
        throw new IllegalArgumentException("Unsupported event type");
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
        throw new IllegalArgumentException("Unsupported event type");
    }

    public <T extends MasterEvent> Observable<T> update(@NonNull T event) {
        return preprocessOnUpdate(event)
                .flatMap(this::validate)
                .flatMap(this::createImageFile)
                .flatMap(this::updateInternal)
                .flatMap(this::postprocessOnUpdate);
    }

    public <T extends MasterEvent> Observable<T> updateLinearGroup(@NonNull T event, List<LinearGroupFieldType> fields) {
        return preprocessOnUpdate(event)
                .flatMap(this::validate)
                .flatMap(this::createImageFile)
                .flatMap(processedEvent -> updateLinearGroupInternal(processedEvent, fields))
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
                    .fields(Collections.emptyList())
                    .build())
                    .flatMap(this::deleteImageFiles)
                    .map(UpdateDoctorVisitEventResponse::getDoctorVisitEvent);
        } else if (event.getEventType() == EventType.MEDICINE_TAKING) {
            return (Observable<T>) calendarRepository.update(UpdateMedicineTakingEventRequest.builder()
                    .medicineTakingEvent((MedicineTakingEvent) event)
                    .fields(Collections.emptyList())
                    .build())
                    .flatMap(this::deleteImageFiles)
                    .map(UpdateMedicineTakingEventResponse::getMedicineTakingEvent);
        } else if (event.getEventType() == EventType.EXERCISE) {
            return (Observable<T>) calendarRepository.update(UpdateExerciseEventRequest.builder()
                    .exerciseEvent((ExerciseEvent) event)
                    .fields(Collections.emptyList())
                    .build())
                    .flatMap(this::deleteImageFiles)
                    .map(UpdateExerciseEventResponse::getExerciseEvent);
        }
        throw new IllegalArgumentException("Unsupported event type");
    }

    @SuppressWarnings("unchecked")
    private <T extends MasterEvent> Observable<T> updateLinearGroupInternal(@NonNull T event, @NonNull List<LinearGroupFieldType> fields) {
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
            return (Observable<T>) calendarRepository.update(UpdateDoctorVisitEventRequest.builder()
                    .doctorVisitEvent((DoctorVisitEvent) event)
                    .fields(fields)
                    .build())
                    .flatMap(this::deleteImageFiles)
                    .map(UpdateDoctorVisitEventResponse::getDoctorVisitEvent);
        } else if (event.getEventType() == EventType.MEDICINE_TAKING) {
            return (Observable<T>) calendarRepository.update(UpdateMedicineTakingEventRequest.builder()
                    .medicineTakingEvent((MedicineTakingEvent) event)
                    .fields(fields)
                    .build())
                    .flatMap(this::deleteImageFiles)
                    .map(UpdateMedicineTakingEventResponse::getMedicineTakingEvent);
        } else if (event.getEventType() == EventType.EXERCISE) {
            return (Observable<T>) calendarRepository.update(UpdateExerciseEventRequest.builder()
                    .exerciseEvent((ExerciseEvent) event)
                    .fields(fields)
                    .build())
                    .flatMap(this::deleteImageFiles)
                    .map(UpdateExerciseEventResponse::getExerciseEvent);
        }
        throw new IllegalArgumentException("Unsupported event type");
    }

    public <T extends MasterEvent> Observable<T> delete(@NonNull T event) {
        return calendarRepository.delete(event)
                .flatMap(imageFilesToDelete -> deleteImageFiles(event, imageFilesToDelete));
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
        } else if (event.getEventType() == EventType.EXERCISE) {
            ExerciseEvent exerciseEvent = (ExerciseEvent) event;
            ConcreteExercise concreteExercise = exerciseEvent.getConcreteExercise();
            Integer linearGroup = exerciseEvent.getLinearGroup();
            return calendarRepository.deleteLinearGroup(DeleteConcreteExerciseEventsRequest.builder()
                    .concreteExercise(concreteExercise)
                    .linearGroup(linearGroup)
                    .build())
                    .flatMap(this::deleteImageFiles)
                    .map(DeleteConcreteExerciseEventsResponse::getCount);
        }
        throw new IllegalArgumentException("Unsupported event type");
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
        boolean isDone = EventUtils.isDone(event);
        MasterEvent masterEvent = event.toMasterBuilder().isDone(!isDone).build();
        return calendarRepository.updateMasterEvent(masterEvent);
    }

    public Observable<MasterEvent> move(@NonNull MasterEvent event, int minutes) {
        DateTime dateTime = event.getDateTime();
        MasterEvent masterEvent = event.toMasterBuilder().dateTime(dateTime.plusMinutes(minutes)).build();
        return calendarRepository.updateMasterEvent(masterEvent);
    }

    public Observable<MasterEvent> moveLinearGroup(@NonNull MasterEvent event, int minutes) {
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
            return calendarRepository.update(UpdateDoctorVisitEventRequest.builder()
                    .doctorVisitEvent((DoctorVisitEvent) event)
                    .fields(Collections.singletonList(LinearGroupFieldType.TIME))
                    .minutes(minutes)
                    .build())
                    .map(UpdateDoctorVisitEventResponse::getDoctorVisitEvent);
        } else if (event.getEventType() == EventType.MEDICINE_TAKING) {
            return calendarRepository.update(UpdateMedicineTakingEventRequest.builder()
                    .medicineTakingEvent((MedicineTakingEvent) event)
                    .fields(Collections.singletonList(LinearGroupFieldType.TIME))
                    .minutes(minutes)
                    .build())
                    .map(UpdateMedicineTakingEventResponse::getMedicineTakingEvent);
        } else if (event.getEventType() == EventType.EXERCISE) {
            return calendarRepository.update(UpdateExerciseEventRequest.builder()
                    .exerciseEvent((ExerciseEvent) event)
                    .fields(Collections.singletonList(LinearGroupFieldType.TIME))
                    .minutes(minutes)
                    .build())
                    .map(UpdateExerciseEventResponse::getExerciseEvent);
        }
        throw new IllegalArgumentException("Unsupported event type");
    }

    public Observable<Boolean> controlOtherEventDoneButton(@NonNull Observable<TextViewAfterTextChangeEvent> otherEventNameObservable) {
        return otherEventNameObservable
                .map(TextViewAfterTextChangeEvent::editable)
                .map(Editable::toString)
                .map(String::trim)
                .map(otherEventName -> !TextUtils.isEmpty(otherEventName))
                .distinctUntilChanged();
    }

    public Observable<List<CalendarValidationResult>> controlOtherEventFields(@NonNull Observable<TextViewAfterTextChangeEvent> otherEventNameObservable) {
        return otherEventNameObservable
                .map(TextViewAfterTextChangeEvent::editable)
                .map(Editable::toString)
                .map(String::trim)
                .map(otherEventName -> OtherEvent.builder().name(otherEventName).build())
                .map(otherEventValidator::validate);
    }

    public Observable<List<CalendarValidationResult>> controlDoctorVisitEventFields(@NonNull Observable<TextViewAfterTextChangeEvent> doctorVisitEventNameObservable) {
        return doctorVisitEventNameObservable
                .map(TextViewAfterTextChangeEvent::editable)
                .map(Editable::toString)
                .map(String::trim)
                .map(doctorVisitEventName -> DoctorVisitEvent.builder().name(doctorVisitEventName).build())
                .map(doctorVisitEventValidator::validate);
    }

    public Observable<List<CalendarValidationResult>> controlExerciseEventFields(@NonNull Observable<TextViewAfterTextChangeEvent> exerciseEventNameObservable) {
        return exerciseEventNameObservable
                .map(TextViewAfterTextChangeEvent::editable)
                .map(Editable::toString)
                .map(String::trim)
                .map(exerciseEventName -> ExerciseEvent.builder().name(exerciseEventName).build())
                .map(exerciseEventValidator::validate);
    }

    private <T extends MasterEvent> Observable<T> validate(@NonNull T item) {
        return getValidator(item).validateObservable(item);
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
            } else if (item.getEventType() == EventType.EXERCISE) {
                ExerciseEvent exerciseEvent = (ExerciseEvent) item;
                if (imagesRepository.isTemporaryImageFile(exerciseEvent.getImageFileName())) {
                    String uniqueImageFileName = imagesRepository.createUniqueImageFileRelativePath(
                            ImageType.EXERCISE_EVENT, exerciseEvent.getImageFileName());
                    //noinspection unchecked
                    return (T) exerciseEvent.toBuilder().imageFileName(uniqueImageFileName).build();
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
        } else if (event.getEventType() == EventType.EXERCISE) {
            return (Validator<T, CalendarValidationResult>) exerciseEventValidator;
        }
        throw new IllegalArgumentException("Unsupported event type");
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
            throw new IllegalArgumentException("Unsupported event type");
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
            } else if (event.getEventType() == EventType.EXERCISE) {
                return event;
            }
            throw new IllegalArgumentException("Unsupported event type");
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
                Intent intent = new Intent(context, TimerService.class);
                intent.putExtra(TimerService.EXTRA_ACTION, TimerService.ACTION_RESUBSCRIBE);
                context.startService(intent);
                return event;
            }
            throw new IllegalArgumentException("Unsupported event type");
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
            } else if (event.getEventType() == EventType.EXERCISE) {
                return event;
            }
            throw new IllegalArgumentException("Unsupported event type");
        });
    }

    private FeedEvent postprocessFeedEventOnUpsert(FeedEvent feedEvent) {
        calendarRepository.setLastFeedType(feedEvent.getFeedType());
        calendarRepository.setLastFoodMeasure(feedEvent.getFoodMeasure());
        calendarRepository.setLastFood(feedEvent.getFood());
        return feedEvent;
    }

    public Observable<List<Integer>> getFrequencyList(@NonNull EventType eventType) {
        return calendarRepository.getFrequencyList(eventType);
    }

    public Observable<List<PeriodicityType>> getPeriodicityList() {
        return calendarRepository.getPeriodicityList();
    }

    public Observable<Map<TimeUnit, List<Integer>>> getTimeUnitValues() {
        return calendarRepository.getTimeUnitValues();
    }

    public Observable<GetEventsFilter> getSelectedFilterValue() {
        return filterRepository.getSelectedValue();
    }

    public void setSelectedFilterValue(@NonNull GetEventsFilter value) {
        filterRepository.setSelectedValue(value);
    }

    public Observable<GetEventsFilter> getSelectedFilterValueOnce() {
        return filterRepository.getSelectedValueOnce();
    }

    public Observable<GetEventsFilter> setSelectedFilterValueObservable(@NonNull GetEventsFilter value) {
        return filterRepository.setSelectedValueObservable(value);
    }
}
