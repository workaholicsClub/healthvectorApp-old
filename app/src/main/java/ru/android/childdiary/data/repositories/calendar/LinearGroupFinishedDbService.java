package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import lombok.val;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.db.entities.calendar.DoctorVisitEventEntity;
import ru.android.childdiary.data.db.entities.calendar.ExerciseEventEntity;
import ru.android.childdiary.data.db.entities.calendar.MedicineTakingEventEntity;
import ru.android.childdiary.data.db.entities.calendar.core.MasterEventEntity;
import ru.android.childdiary.data.repositories.calendar.mappers.DoctorVisitEventMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.ExerciseEventMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.MedicineTakingEventMapper;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.calendar.data.DoctorVisitEvent;
import ru.android.childdiary.domain.calendar.data.ExerciseEvent;
import ru.android.childdiary.domain.calendar.data.MedicineTakingEvent;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.exercises.data.ConcreteExercise;
import ru.android.childdiary.domain.medical.data.DoctorVisit;
import ru.android.childdiary.domain.medical.data.MedicineTaking;
import ru.android.childdiary.utils.strings.DateUtils;

@Singleton
public class LinearGroupFinishedDbService {
    private final ReactiveEntityStore<Persistable> dataStore;
    private final DoctorVisitEventMapper doctorVisitEventMapper;
    private final MedicineTakingEventMapper medicineTakingEventMapper;
    private final ExerciseEventMapper exerciseEventMapper;

    @Inject
    public LinearGroupFinishedDbService(ReactiveEntityStore<Persistable> dataStore,
                                        DoctorVisitEventMapper doctorVisitEventMapper,
                                        MedicineTakingEventMapper medicineTakingEventMapper,
                                        ExerciseEventMapper exerciseEventMapper) {
        this.dataStore = dataStore;
        this.doctorVisitEventMapper = doctorVisitEventMapper;
        this.medicineTakingEventMapper = medicineTakingEventMapper;
        this.exerciseEventMapper = exerciseEventMapper;
    }

    public Observable<List<MasterEvent>> getFinishedLinearGroupEvents() {
        LocalDate date = LocalDate.now();
        return Observable.combineLatest(
                getDoctorVisitEvents(date).first(Collections.emptyList()).toObservable(),
                getMedicineTakingEvents(date).first(Collections.emptyList()).toObservable(),
                getExerciseEvents(date).first(Collections.emptyList()).toObservable(),
                (doctorVisitEvents, medicineTakingEvents, exerciseEvents) ->
                        concat(Arrays.asList(doctorVisitEvents, medicineTakingEvents, exerciseEvents))
        );
    }

    private List<MasterEvent> concat(@NonNull List<List<? extends MasterEvent>> lists) {
        List<MasterEvent> events = new ArrayList<>();
        //noinspection Convert2streamapi
        for (val list : lists) {
            events.addAll(list);
        }
        return events;
    }

    private Observable<List<DoctorVisitEvent>> getDoctorVisitEvents(@NonNull LocalDate date) {
        return dataStore.select(DoctorVisitEventEntity.class)
                .join(MasterEventEntity.class).on(DoctorVisitEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .and(MasterEventEntity.EVENT_TYPE.eq(EventType.DOCTOR_VISIT))
                .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(DateUtils.midnight(date)))
                .and(MasterEventEntity.DATE_TIME.lessThan(DateUtils.nextDayMidnight(date)))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, doctorVisitEventMapper))
                .flatMap(Observable::fromIterable)
                .filter(event -> filter(event, date))
                .toList()
                .toObservable();
    }

    private boolean filter(@NonNull DoctorVisitEvent event, @NonNull LocalDate date) {
        DoctorVisit doctorVisit = event.getDoctorVisit();
        Integer linearGroup = event.getLinearGroup();
        return linearGroup == null ? false : dataStore.count(DoctorVisitEventEntity.class)
                .join(MasterEventEntity.class).on(DoctorVisitEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .and(MasterEventEntity.EVENT_TYPE.eq(EventType.DOCTOR_VISIT))
                .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(DateUtils.nextDayMidnight(date)))
                .and(MasterEventEntity.LINEAR_GROUP.eq(linearGroup))
                .and(DoctorVisitEventEntity.DOCTOR_VISIT_ID.eq(doctorVisit.getId()))
                .get()
                .single()
                .map(count -> count == 0)
                .blockingGet();
    }

    private Observable<List<MedicineTakingEvent>> getMedicineTakingEvents(@NonNull LocalDate date) {
        return dataStore.select(MedicineTakingEventEntity.class)
                .join(MasterEventEntity.class).on(MedicineTakingEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .and(MasterEventEntity.EVENT_TYPE.eq(EventType.MEDICINE_TAKING))
                .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(DateUtils.midnight(date)))
                .and(MasterEventEntity.DATE_TIME.lessThan(DateUtils.nextDayMidnight(date)))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, medicineTakingEventMapper))
                .flatMap(Observable::fromIterable)
                .filter(event -> filter(event, date))
                .toList()
                .toObservable();
    }

    private boolean filter(@NonNull MedicineTakingEvent event, @NonNull LocalDate date) {
        MedicineTaking medicineTaking = event.getMedicineTaking();
        Integer linearGroup = event.getLinearGroup();
        return linearGroup == null ? false : dataStore.count(MedicineTakingEventEntity.class)
                .join(MasterEventEntity.class).on(MedicineTakingEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .and(MasterEventEntity.EVENT_TYPE.eq(EventType.MEDICINE_TAKING))
                .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(DateUtils.nextDayMidnight(date)))
                .and(MasterEventEntity.LINEAR_GROUP.eq(linearGroup))
                .and(MedicineTakingEventEntity.MEDICINE_TAKING_ID.eq(medicineTaking.getId()))
                .get()
                .single()
                .map(count -> count == 0)
                .blockingGet();
    }

    private Observable<List<ExerciseEvent>> getExerciseEvents(@NonNull LocalDate date) {
        return dataStore.select(ExerciseEventEntity.class)
                .join(MasterEventEntity.class).on(ExerciseEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .and(MasterEventEntity.EVENT_TYPE.eq(EventType.EXERCISE))
                .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(DateUtils.midnight(date)))
                .and(MasterEventEntity.DATE_TIME.lessThan(DateUtils.nextDayMidnight(date)))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, exerciseEventMapper))
                .flatMap(Observable::fromIterable)
                .filter(event -> filter(event, date))
                .toList()
                .toObservable();
    }

    private boolean filter(@NonNull ExerciseEvent event, @NonNull LocalDate date) {
        ConcreteExercise concreteExercise = event.getConcreteExercise();
        Integer linearGroup = event.getLinearGroup();
        return linearGroup == null ? false : dataStore.count(ExerciseEventEntity.class)
                .join(MasterEventEntity.class).on(ExerciseEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .and(MasterEventEntity.EVENT_TYPE.eq(EventType.EXERCISE))
                .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(DateUtils.nextDayMidnight(date)))
                .and(MasterEventEntity.LINEAR_GROUP.eq(linearGroup))
                .and(ExerciseEventEntity.CONCRETE_EXERCISE_ID.eq(concreteExercise.getId()))
                .get()
                .single()
                .map(count -> count == 0)
                .blockingGet();
    }
}
