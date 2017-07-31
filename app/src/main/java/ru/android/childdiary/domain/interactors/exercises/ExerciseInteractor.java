package ru.android.childdiary.domain.interactors.exercises;

import android.support.annotation.NonNull;
import android.text.Editable;

import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.childdiary.data.repositories.calendar.CalendarDataRepository;
import ru.android.childdiary.data.repositories.child.ChildDataRepository;
import ru.android.childdiary.data.repositories.core.settings.SettingsDataRepository;
import ru.android.childdiary.data.repositories.exercises.ExerciseDataRepository;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.core.validation.EventValidationResult;
import ru.android.childdiary.domain.interactors.calendar.CalendarRepository;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildRepository;
import ru.android.childdiary.domain.interactors.core.LengthValue;
import ru.android.childdiary.domain.interactors.core.LinearGroups;
import ru.android.childdiary.domain.interactors.core.PeriodicityType;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.core.settings.SettingsRepository;
import ru.android.childdiary.domain.interactors.exercises.requests.UpsertConcreteExerciseRequest;
import ru.android.childdiary.domain.interactors.exercises.requests.UpsertConcreteExerciseResponse;
import ru.android.childdiary.domain.interactors.exercises.validation.ConcreteExerciseValidator;
import ru.android.childdiary.presentation.core.bindings.FieldValueChangeEventsObservable;

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
        return Observable.combineLatest(
                getDefaultRepeatParameters(),
                Observable.just(DateTime.now()),
                calendarRepository.getDefaultNotifyTimeInMinutes(EventType.EXERCISE),
                (repeatParameters, dateTime, minutes) -> ConcreteExercise.builder()
                        .child(child)
                        .exercise(exercise)
                        .repeatParameters(repeatParameters)
                        .name(exercise.getName())
                        .durationInMinutes(15)
                        .dateTime(dateTime)
                        .finishDateTime(null)
                        .isExported(true)
                        .notifyTimeInMinutes(minutes)
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
                .map(concreteExerciseValidator::validate)
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
                .map(concreteExerciseValidator::validate);
    }
}
