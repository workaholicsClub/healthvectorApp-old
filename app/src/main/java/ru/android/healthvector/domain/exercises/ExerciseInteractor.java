package ru.android.healthvector.domain.exercises;

import android.support.annotation.NonNull;
import android.text.Editable;

import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.healthvector.data.repositories.calendar.CalendarDataRepository;
import ru.android.healthvector.data.repositories.child.ChildDataRepository;
import ru.android.healthvector.data.repositories.core.settings.SettingsDataRepository;
import ru.android.healthvector.data.repositories.exercises.ExerciseDataRepository;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.domain.calendar.CalendarRepository;
import ru.android.healthvector.domain.calendar.data.core.LengthValue;
import ru.android.healthvector.domain.calendar.data.core.LinearGroups;
import ru.android.healthvector.domain.calendar.data.core.PeriodicityType;
import ru.android.healthvector.domain.calendar.data.core.RepeatParameters;
import ru.android.healthvector.domain.child.ChildRepository;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.core.settings.SettingsRepository;
import ru.android.healthvector.domain.core.validation.EventValidationResult;
import ru.android.healthvector.domain.exercises.data.ConcreteExercise;
import ru.android.healthvector.domain.exercises.data.Exercise;
import ru.android.healthvector.domain.exercises.requests.UpsertConcreteExerciseRequest;
import ru.android.healthvector.domain.exercises.requests.UpsertConcreteExerciseResponse;
import ru.android.healthvector.domain.exercises.validation.ConcreteExerciseValidator;
import ru.android.healthvector.presentation.core.bindings.FieldValueChangeEventsObservable;

public class ExerciseInteractor {
    private final ChildRepository childRepository;
    private final ExerciseRepository exerciseRepository;
    private final CalendarRepository calendarRepository;
    private final SettingsRepository settingsRepository;
    private final ConcreteExerciseValidator concreteExerciseValidator;

    @Inject
    public ExerciseInteractor(ChildDataRepository childRepository,
                              ExerciseDataRepository exerciseRepository,
                              CalendarDataRepository calendarRepository,
                              SettingsDataRepository settingsRepository,
                              ConcreteExerciseValidator concreteExerciseValidator) {
        this.childRepository = childRepository;
        this.exerciseRepository = exerciseRepository;
        this.calendarRepository = calendarRepository;
        this.settingsRepository = settingsRepository;
        this.concreteExerciseValidator = concreteExerciseValidator;
    }

    public Observable<Exercise> getExercise(@NonNull Exercise exercise) {
        return exerciseRepository.getExercise(exercise);
    }

    public Observable<List<Exercise>> getExercises(@NonNull Child child) {
        return exerciseRepository.getExercises(child);
    }

    public Observable<List<Exercise>> updateExercises() {
        return exerciseRepository.updateExercises();
    }

    public Observable<List<Exercise>> updateExercisesIfNeeded() {
        return exerciseRepository.updateExercisesIfNeeded();
    }

    public Observable<ConcreteExercise> getDefaultConcreteExercise(@NonNull Child child, @NonNull Exercise exercise) {
        EventType eventType = EventType.EXERCISE;
        return Observable.combineLatest(
                getDefaultRepeatParameters(),
                Observable.just(DateTime.now()),
                calendarRepository.getNotificationSettingsOnce(eventType),
                (repeatParameters, dateTime, eventNotification) -> ConcreteExercise.builder()
                        .child(child)
                        .exercise(exercise)
                        .repeatParameters(repeatParameters)
                        .name(exercise.getName())
                        .durationInMinutes(15)
                        .dateTime(dateTime)
                        .finishDateTime(null)
                        .isExported(true)
                        .notifyTimeInMinutes(eventNotification.getNotifyTime())
                        .note(null)
                        .imageFileName(null)
                        .isDeleted(null)
                        .build());
    }

    private Observable<RepeatParameters> getDefaultRepeatParameters() {
        return Observable.just(
                RepeatParameters.builder()
                        .frequency(LinearGroups.builder()
                                .times(Collections.emptyList())
                                .build())
                        .periodicity(null)
                        .length(null)
                        .build());
    }

    public Observable<LocalTime> getStartTimeOnce() {
        return settingsRepository.getStartTimeOnce();
    }

    public Observable<LocalTime> getFinishTimeOnce() {
        return settingsRepository.getFinishTimeOnce();
    }

    public Single<Boolean> hasConnectedEvents(@NonNull Exercise exercise) {
        return exerciseRepository.hasConnectedEvents(exercise);
    }

    private Observable<UpsertConcreteExerciseRequest> validate(@NonNull UpsertConcreteExerciseRequest request) {
        return concreteExerciseValidator.validateObservable(request.getConcreteExercise())
                .map(concreteExercise -> request.toBuilder().concreteExercise(concreteExercise).build());
    }

    public Observable<UpsertConcreteExerciseResponse> addConcreteExercise(@NonNull UpsertConcreteExerciseRequest request) {
        return validate(request)
                .flatMap(exerciseRepository::addConcreteExercise);
    }

    public Observable<Boolean> controlDoneButton(
            @NonNull Observable<TextViewAfterTextChangeEvent> concreteExerciseNameObservable,
            @NonNull FieldValueChangeEventsObservable<LinearGroups> linearGroupsObservable,
            @NonNull FieldValueChangeEventsObservable<PeriodicityType> periodicityTypeObservable,
            @NonNull FieldValueChangeEventsObservable<LengthValue> lengthValueObservable) {
        return Observable.combineLatest(
                concreteExerciseNameObservable
                        .map(TextViewAfterTextChangeEvent::editable)
                        .map(Editable::toString)
                        .map(String::trim),
                linearGroupsObservable,
                periodicityTypeObservable,
                lengthValueObservable,
                (concreteExerciseName, linearGroupsEvent, periodicityTypeEvent, lengthValueEvent) -> ConcreteExercise.builder()
                        .name(concreteExerciseName)
                        .isExported(true)
                        .repeatParameters(RepeatParameters.builder()
                                .frequency(linearGroupsEvent.getValue())
                                .periodicity(periodicityTypeEvent.getValue())
                                .length(lengthValueEvent.getValue())
                                .build())
                        .build())
                .map(concreteExerciseValidator::validateOnUi)
                .map(concreteExerciseValidator::isValid)
                .distinctUntilChanged();
    }

    public Observable<List<EventValidationResult>> controlFields(
            @NonNull Observable<TextViewAfterTextChangeEvent> concreteExerciseNameObservable,
            @NonNull FieldValueChangeEventsObservable<LinearGroups> linearGroupsObservable,
            @NonNull FieldValueChangeEventsObservable<PeriodicityType> periodicityTypeObservable,
            @NonNull FieldValueChangeEventsObservable<LengthValue> lengthValueObservable) {
        return Observable.combineLatest(
                concreteExerciseNameObservable
                        .map(TextViewAfterTextChangeEvent::editable)
                        .map(Editable::toString)
                        .map(String::trim),
                linearGroupsObservable,
                periodicityTypeObservable,
                lengthValueObservable,
                (concreteExerciseName, linearGroupsEvent, periodicityTypeEvent, lengthValueEvent) -> ConcreteExercise.builder()
                        .name(concreteExerciseName)
                        .isExported(true)
                        .repeatParameters(RepeatParameters.builder()
                                .frequency(linearGroupsEvent.getValue())
                                .periodicity(periodicityTypeEvent.getValue())
                                .length(lengthValueEvent.getValue())
                                .build())
                        .build())
                .map(concreteExerciseValidator::validateOnUi);
    }

    public Observable<Integer> continueLinearGroup(@NonNull ConcreteExercise concreteExercise,
                                                   @NonNull LocalDate sinceDate,
                                                   @NonNull Integer linearGroup,
                                                   @NonNull LengthValue lengthValue) {
        return exerciseRepository.continueLinearGroup(concreteExercise, sinceDate, linearGroup, lengthValue);
    }
}
