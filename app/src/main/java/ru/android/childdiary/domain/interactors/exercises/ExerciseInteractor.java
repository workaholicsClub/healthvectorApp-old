package ru.android.childdiary.domain.interactors.exercises;

import android.support.annotation.NonNull;
import android.text.Editable;

import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.ArrayList;
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
import ru.android.childdiary.domain.core.validation.EventValidationException;
import ru.android.childdiary.domain.core.validation.EventValidationResult;
import ru.android.childdiary.domain.interactors.calendar.CalendarRepository;
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

    public Observable<List<Exercise>> getExercises() {
        return exerciseRepository.getExercises();
    }

    public Observable<List<Exercise>> updateExercisesIfNeeded() {
        return exerciseRepository.updateExercisesIfNeeded();
    }

    public Observable<ConcreteExercise> getDefaultConcreteExercise() {
        return Observable.combineLatest(
                childRepository.getActiveChildOnce(),
                getDefaultRepeatParameters(),
                Observable.just(DateTime.now()),
                calendarRepository.getDefaultNotifyTimeInMinutes(EventType.EXERCISE),
                (child, repeatParameters, dateTime, minutes) -> ConcreteExercise.builder()
                        .child(child)
                        .repeatParameters(repeatParameters)
                        .name(null)
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
                                .times(new ArrayList<>(Collections.emptyList()))
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
        return Observable.defer(() -> {
            List<EventValidationResult> results = concreteExerciseValidator.validate(request.getConcreteExercise());
            if (!concreteExerciseValidator.isValid(results)) {
                return Observable.error(new EventValidationException(results));
            }
            return Observable.just(request);
        });
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
                        .repeatParameters(RepeatParameters.builder()
                                .frequency(linearGroupsEvent.getValue())
                                .periodicity(periodicityTypeEvent.getValue())
                                .length(lengthValueEvent.getValue())
                                .build())
                        .build())
                .map(concreteExerciseValidator::validate);
    }
}
