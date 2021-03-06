package ru.android.healthvector.data.repositories.calendar;

import android.support.annotation.NonNull;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import ru.android.healthvector.data.db.DbUtils;
import ru.android.healthvector.data.db.entities.calendar.DoctorVisitEventEntity;
import ru.android.healthvector.data.db.entities.calendar.ExerciseEventEntity;
import ru.android.healthvector.data.db.entities.calendar.MedicineTakingEventEntity;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventEntity;
import ru.android.healthvector.data.repositories.calendar.mappers.DoctorVisitEventMapper;
import ru.android.healthvector.data.repositories.calendar.mappers.ExerciseEventMapper;
import ru.android.healthvector.data.repositories.calendar.mappers.MedicineTakingEventMapper;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.domain.calendar.data.DoctorVisitEvent;
import ru.android.healthvector.domain.calendar.data.ExerciseEvent;
import ru.android.healthvector.domain.calendar.data.MedicineTakingEvent;
import ru.android.healthvector.domain.calendar.data.core.MasterEvent;
import ru.android.healthvector.domain.exercises.data.ConcreteExercise;
import ru.android.healthvector.domain.medical.data.DoctorVisit;
import ru.android.healthvector.domain.medical.data.MedicineTaking;
import ru.android.healthvector.utils.strings.DateUtils;

@Singleton
public class LinearGroupFinishedDbService {
    private final Logger logger = LoggerFactory.getLogger(toString());
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

    public Observable<List<MasterEvent>> getFinishedLinearGroupEvents(@NonNull LocalDate lastCheckedDate,
                                                                      @NonNull LocalDate dateToCheck) {
        logger.debug("last checked date: " + lastCheckedDate + "; date to check: " + dateToCheck);
        lastCheckedDate = lastCheckedDate.plusDays(1);
        int days = Days.daysBetween(lastCheckedDate, dateToCheck).getDays();
        if (days > 6) {
            lastCheckedDate = dateToCheck.minusDays(6);
        } else if (days < 0) {
            lastCheckedDate = dateToCheck;
        }
        logger.debug("first date inclusive: " + lastCheckedDate + "; last date inclusive: " + dateToCheck);
        return Observable.combineLatest(
                getDoctorVisitEvents(lastCheckedDate, dateToCheck).first(Collections.emptyList()).toObservable(),
                getMedicineTakingEvents(lastCheckedDate, dateToCheck).first(Collections.emptyList()).toObservable(),
                getExerciseEvents(lastCheckedDate, dateToCheck).first(Collections.emptyList()).toObservable(),
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

    private Observable<List<DoctorVisitEvent>> getDoctorVisitEvents(@NonNull LocalDate firstDateInclusive,
                                                                    @NonNull LocalDate lastDateInclusive) {
        return dataStore.select(DoctorVisitEventEntity.class)
                .join(MasterEventEntity.class).on(DoctorVisitEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .and(MasterEventEntity.EVENT_TYPE.eq(EventType.DOCTOR_VISIT))
                .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(DateUtils.midnight(firstDateInclusive)))
                .and(MasterEventEntity.DATE_TIME.lessThan(DateUtils.nextDayMidnight(lastDateInclusive)))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, doctorVisitEventMapper))
                .map(events -> mapDoctorVisitEvents(events, lastDateInclusive));
    }

    private List<DoctorVisitEvent> mapDoctorVisitEvents(@NonNull List<DoctorVisitEvent> events,
                                                        @NonNull LocalDate afterDate) {
        return Observable.fromIterable(events)
                .filter(this::filter)
                .toList()
                .blockingGet();
    }

    private boolean filter(@NonNull DoctorVisitEvent event) {
        DoctorVisit doctorVisit = event.getDoctorVisit();
        Integer linearGroup = event.getLinearGroup();
        return linearGroup == null ? false : dataStore.count(DoctorVisitEventEntity.class)
                .join(MasterEventEntity.class).on(DoctorVisitEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .and(MasterEventEntity.EVENT_TYPE.eq(EventType.DOCTOR_VISIT))
                .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(DateUtils.nextDayMidnight(event.getDateTime().toLocalDate())))
                .and(MasterEventEntity.LINEAR_GROUP.eq(linearGroup))
                .and(DoctorVisitEventEntity.DOCTOR_VISIT_ID.eq(doctorVisit.getId()))
                .get()
                .single()
                .map(count -> count == 0)
                .blockingGet();
    }

    private Observable<List<MedicineTakingEvent>> getMedicineTakingEvents(@NonNull LocalDate firstDateInclusive,
                                                                          @NonNull LocalDate lastDateInclusive) {
        return dataStore.select(MedicineTakingEventEntity.class)
                .join(MasterEventEntity.class).on(MedicineTakingEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .and(MasterEventEntity.EVENT_TYPE.eq(EventType.MEDICINE_TAKING))
                .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(DateUtils.midnight(firstDateInclusive)))
                .and(MasterEventEntity.DATE_TIME.lessThan(DateUtils.nextDayMidnight(lastDateInclusive)))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, medicineTakingEventMapper))
                .map(events -> mapMedicineTakingEvents(events, lastDateInclusive));
    }

    private List<MedicineTakingEvent> mapMedicineTakingEvents(@NonNull List<MedicineTakingEvent> events,
                                                              @NonNull LocalDate afterDate) {
        return Observable.fromIterable(events)
                .filter(this::filter)
                .toList()
                .blockingGet();
    }

    private boolean filter(@NonNull MedicineTakingEvent event) {
        MedicineTaking medicineTaking = event.getMedicineTaking();
        Integer linearGroup = event.getLinearGroup();
        return linearGroup == null ? false : dataStore.count(MedicineTakingEventEntity.class)
                .join(MasterEventEntity.class).on(MedicineTakingEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .and(MasterEventEntity.EVENT_TYPE.eq(EventType.MEDICINE_TAKING))
                .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(DateUtils.nextDayMidnight(event.getDateTime().toLocalDate())))
                .and(MasterEventEntity.LINEAR_GROUP.eq(linearGroup))
                .and(MedicineTakingEventEntity.MEDICINE_TAKING_ID.eq(medicineTaking.getId()))
                .get()
                .single()
                .map(count -> count == 0)
                .blockingGet();
    }

    private Observable<List<ExerciseEvent>> getExerciseEvents(@NonNull LocalDate firstDateInclusive,
                                                              @NonNull LocalDate lastDateInclusive) {
        return dataStore.select(ExerciseEventEntity.class)
                .join(MasterEventEntity.class).on(ExerciseEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .and(MasterEventEntity.EVENT_TYPE.eq(EventType.EXERCISE))
                .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(DateUtils.midnight(firstDateInclusive)))
                .and(MasterEventEntity.DATE_TIME.lessThan(DateUtils.nextDayMidnight(lastDateInclusive)))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, exerciseEventMapper))
                .map(events -> mapExerciseEvents(events, lastDateInclusive));
    }

    private List<ExerciseEvent> mapExerciseEvents(@NonNull List<ExerciseEvent> events,
                                                  @NonNull LocalDate afterDate) {
        return Observable.fromIterable(events)
                .filter(this::filter)
                .toList()
                .blockingGet();
    }

    private boolean filter(@NonNull ExerciseEvent event) {
        ConcreteExercise concreteExercise = event.getConcreteExercise();
        Integer linearGroup = event.getLinearGroup();
        return linearGroup == null ? false : dataStore.count(ExerciseEventEntity.class)
                .join(MasterEventEntity.class).on(ExerciseEventEntity.MASTER_EVENT_ID.eq(MasterEventEntity.ID))
                .and(MasterEventEntity.EVENT_TYPE.eq(EventType.EXERCISE))
                .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(DateUtils.nextDayMidnight(event.getDateTime().toLocalDate())))
                .and(MasterEventEntity.LINEAR_GROUP.eq(linearGroup))
                .and(ExerciseEventEntity.CONCRETE_EXERCISE_ID.eq(concreteExercise.getId()))
                .get()
                .single()
                .map(count -> count == 0)
                .blockingGet();
    }
}
