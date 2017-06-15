package ru.android.childdiary.data.repositories.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import io.reactivex.Observable;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.query.WhereAndOr;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.entities.calendar.events.DoctorVisitEventEntity;
import ru.android.childdiary.data.entities.calendar.events.ExerciseEventEntity;
import ru.android.childdiary.data.entities.calendar.events.MedicineTakingEventEntity;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.entities.exercises.ConcreteExerciseEntity;
import ru.android.childdiary.data.entities.medical.DoctorVisitEntity;
import ru.android.childdiary.data.entities.medical.MedicineTakingEntity;
import ru.android.childdiary.data.entities.medical.core.DoctorEntity;
import ru.android.childdiary.data.entities.medical.core.MedicineEntity;
import ru.android.childdiary.data.entities.medical.core.MedicineMeasureEntity;
import ru.android.childdiary.domain.interactors.calendar.events.DoctorVisitEvent;
import ru.android.childdiary.domain.interactors.calendar.events.ExerciseEvent;
import ru.android.childdiary.domain.interactors.calendar.events.MedicineTakingEvent;
import ru.android.childdiary.domain.interactors.exercises.ConcreteExercise;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;

public abstract class EventsDbService {
    protected final Logger logger = LoggerFactory.getLogger(toString());
    protected final ReactiveEntityStore<Persistable> dataStore;
    protected final BlockingEntityStore<Persistable> blockingEntityStore;

    public EventsDbService(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
        this.blockingEntityStore = dataStore.toBlocking();
    }

    protected Long getDoctorVisitId(@NonNull DoctorVisit doctorVisit) {
        Long id = doctorVisit.getId();
        if (id == null) {
            throw new IllegalStateException("Doctor visit id is null");
        }
        return id;
    }

    protected Long getDoctorVisitId(@NonNull DoctorVisitEvent doctorVisitEvent) {
        DoctorVisit doctorVisit = doctorVisitEvent.getDoctorVisit();
        if (doctorVisit == null) {
            throw new IllegalStateException("Doctor visit is null");
        }
        return getDoctorVisitId(doctorVisit);
    }

    protected Long getMedicineTakingId(@NonNull MedicineTaking medicineTaking) {
        Long id = medicineTaking.getId();
        if (id == null) {
            throw new IllegalStateException("Medicine taking id is null");
        }
        return id;
    }

    protected Long getMedicineTakingId(@NonNull MedicineTakingEvent medicineTakingEvent) {
        MedicineTaking medicineTaking = medicineTakingEvent.getMedicineTaking();
        if (medicineTaking == null) {
            throw new IllegalStateException("Medicine taking is null");
        }
        return getMedicineTakingId(medicineTaking);
    }

    protected Long getConcreteExerciseId(@NonNull ConcreteExercise concreteExercise) {
        Long id = concreteExercise.getId();
        if (id == null) {
            throw new IllegalStateException("Concrete exercise id is null");
        }
        return id;
    }

    protected Long getConcreteExerciseId(@NonNull ExerciseEvent exerciseEvent) {
        ConcreteExercise concreteExercise = exerciseEvent.getConcreteExercise();
        if (concreteExercise == null) {
            throw new IllegalStateException("Concrete exercise is null");
        }
        return getConcreteExerciseId(concreteExercise);
    }

    protected DoctorVisitEntity findDoctorVisitEntity(Long id) {
        return blockingEntityStore.findByKey(DoctorVisitEntity.class, id);
    }

    @Nullable
    protected DoctorEntity findDoctorEntity(@Nullable Doctor doctor) {
        if (doctor == null) {
            return null;
        }
        return blockingEntityStore.findByKey(DoctorEntity.class, doctor.getId());
    }

    protected MedicineTakingEntity findMedicineTakingEntity(Long id) {
        return blockingEntityStore.findByKey(MedicineTakingEntity.class, id);
    }

    @Nullable
    protected MedicineEntity findMedicineEntity(@Nullable Medicine medicine) {
        if (medicine == null) {
            return null;
        }
        return blockingEntityStore.findByKey(MedicineEntity.class, medicine.getId());
    }

    @Nullable
    protected MedicineMeasureEntity findMedicineMeasureEntity(@Nullable MedicineMeasure medicineMeasure) {
        if (medicineMeasure == null) {
            return null;
        }
        return blockingEntityStore.findByKey(MedicineMeasureEntity.class, medicineMeasure.getId());
    }

    @Nullable
    protected ConcreteExerciseEntity findConcreteExerciseEntity(Long id) {
        return blockingEntityStore.findByKey(ConcreteExerciseEntity.class, id);
    }

    protected MasterEventEntity findMasterEventEntity(@NonNull DoctorVisitEventEntity doctorVisitEventEntity) {
        Long masterEventId = doctorVisitEventEntity.getMasterEvent().getId();
        return blockingEntityStore.findByKey(MasterEventEntity.class, masterEventId);
    }

    protected MasterEventEntity findMasterEventEntity(@NonNull MedicineTakingEventEntity medicineTakingEventEntity) {
        Long masterEventId = medicineTakingEventEntity.getMasterEvent().getId();
        return blockingEntityStore.findByKey(MasterEventEntity.class, masterEventId);
    }

    protected List<DoctorVisitEventEntity> getDoctorVisitEvents(Long id) {
        return selectDoctorVisitEvents(id).get().toList();
    }

    protected List<DoctorVisitEventEntity> getDoctorVisitEvents(Long id, Integer linearGroup) {
        return selectDoctorVisitEvents(id)
                .and(MasterEventEntity.LINEAR_GROUP.eq(linearGroup))
                .get().toList();
    }

    protected List<DoctorVisitEventEntity> getDoctorVisitEvents(Long id, DateTime dateTime) {
        return selectDoctorVisitEvents(id)
                .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(dateTime.toDateTime()))
                .get().toList();
    }

    protected List<MedicineTakingEventEntity> getMedicineTakingEvents(Long id) {
        return selectMedicineTakingEvents(id).get().toList();
    }

    protected List<MedicineTakingEventEntity> getMedicineTakingEvents(Long id, Integer linearGroup) {
        return selectMedicineTakingEvents(id)
                .and(MasterEventEntity.LINEAR_GROUP.eq(linearGroup))
                .get().toList();
    }

    protected List<MedicineTakingEventEntity> getMedicineTakingEvents(Long id, DateTime dateTime) {
        return selectMedicineTakingEvents(id)
                .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(dateTime.toDateTime()))
                .get().toList();
    }

    protected List<ExerciseEventEntity> getExerciseEvents(Long id) {
        return selectExerciseEvents(id).get().toList();
    }

    protected List<ExerciseEventEntity> getExerciseEvents(Long id, Integer linearGroup) {
        return selectExerciseEvents(id)
                .and(MasterEventEntity.LINEAR_GROUP.eq(linearGroup))
                .get().toList();
    }

    protected List<ExerciseEventEntity> getExerciseEvents(Long id, DateTime dateTime) {
        return selectExerciseEvents(id)
                .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(dateTime.toDateTime()))
                .get().toList();
    }

    private WhereAndOr<? extends Result<DoctorVisitEventEntity>> selectDoctorVisitEvents(Long id) {
        return blockingEntityStore
                .select(DoctorVisitEventEntity.class)
                .join(MasterEventEntity.class).on(MasterEventEntity.ID.eq(DoctorVisitEventEntity.MASTER_EVENT_ID))
                .where(DoctorVisitEventEntity.DOCTOR_VISIT_ID.eq(id));
    }

    private WhereAndOr<? extends Result<MedicineTakingEventEntity>> selectMedicineTakingEvents(Long id) {
        return blockingEntityStore
                .select(MedicineTakingEventEntity.class)
                .join(MasterEventEntity.class).on(MasterEventEntity.ID.eq(MedicineTakingEventEntity.MASTER_EVENT_ID))
                .where(MedicineTakingEventEntity.MEDICINE_TAKING_ID.eq(id));
    }

    private WhereAndOr<? extends Result<ExerciseEventEntity>> selectExerciseEvents(Long id) {
        return blockingEntityStore
                .select(ExerciseEventEntity.class)
                .join(MasterEventEntity.class).on(MasterEventEntity.ID.eq(ExerciseEventEntity.MASTER_EVENT_ID))
                .where(ExerciseEventEntity.CONCRETE_EXERCISE_ID.eq(id));
    }

    protected List<MasterEventEntity> getMasterEventsFromDoctorVisitEvents(@NonNull List<DoctorVisitEventEntity> events) {
        return Observable.fromIterable(events)
                .map(event -> blockingEntityStore.findByKey(MasterEventEntity.class,
                        event.getMasterEvent().getId()))
                .toList().blockingGet();
    }

    protected List<MasterEventEntity> getMasterEventsFromMedicineTakingEvents(@NonNull List<MedicineTakingEventEntity> events) {
        return Observable.fromIterable(events)
                .map(event -> blockingEntityStore.findByKey(MasterEventEntity.class,
                        event.getMasterEvent().getId()))
                .toList().blockingGet();
    }

    protected List<MasterEventEntity> getMasterEventsFromExerciseEvents(@NonNull List<ExerciseEventEntity> events) {
        return Observable.fromIterable(events)
                .map(event -> blockingEntityStore.findByKey(MasterEventEntity.class,
                        event.getMasterEvent().getId()))
                .toList().blockingGet();
    }
}
