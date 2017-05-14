package ru.android.childdiary.data.repositories.medical;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import lombok.val;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.entities.calendar.events.DoctorVisitEventEntity;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.entities.medical.DoctorVisitEntity;
import ru.android.childdiary.data.entities.medical.core.DoctorEntity;
import ru.android.childdiary.data.repositories.calendar.mappers.RepeatParametersMapper;
import ru.android.childdiary.data.repositories.core.DoctorVisitEventsGenerator;
import ru.android.childdiary.data.repositories.medical.mappers.DoctorMapper;
import ru.android.childdiary.data.repositories.medical.mappers.DoctorVisitMapper;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.domain.interactors.medical.requests.DoctorVisitsRequest;
import ru.android.childdiary.utils.ObjectUtils;

@Singleton
public class DoctorVisitDbService {
    private final Logger logger = LoggerFactory.getLogger(toString());
    private final ReactiveEntityStore<Persistable> dataStore;
    private final DoctorVisitEventsGenerator eventsGenerator;
    private final DoctorMapper doctorMapper;
    private final DoctorVisitMapper doctorVisitMapper;
    private final RepeatParametersMapper repeatParametersMapper;

    @Inject
    public DoctorVisitDbService(ReactiveEntityStore<Persistable> dataStore,
                                DoctorVisitEventsGenerator eventsGenerator,
                                DoctorMapper doctorMapper,
                                DoctorVisitMapper doctorVisitMapper,
                                RepeatParametersMapper repeatParametersMapper) {
        this.dataStore = dataStore;
        this.eventsGenerator = eventsGenerator;
        this.doctorMapper = doctorMapper;
        this.doctorVisitMapper = doctorVisitMapper;
        this.repeatParametersMapper = repeatParametersMapper;
    }

    public Observable<List<Doctor>> getDoctors() {
        return dataStore.select(DoctorEntity.class)
                .orderBy(DoctorEntity.NAME, DoctorEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, doctorMapper));
    }

    public Observable<Doctor> addDoctor(@NonNull Doctor doctor) {
        return DbUtils.insertObservable(dataStore, doctor, doctorMapper);
    }

    public Observable<Doctor> deleteDoctor(@NonNull Doctor doctor) {
        return DbUtils.deleteObservable(dataStore, DoctorEntity.class, doctor, doctor.getId());
    }

    public Observable<List<DoctorVisit>> getDoctorVisits(@NonNull DoctorVisitsRequest request) {
        Child child = request.getChild();
        return dataStore.select(DoctorVisitEntity.class)
                .where(DoctorVisitEntity.CHILD_ID.eq(child.getId()))
                .and(DoctorVisitEntity.DELETED.isNull().or(DoctorVisitEntity.DELETED.eq(false)))
                .orderBy(DoctorVisitEntity.DATE_TIME, DoctorVisitEntity.DOCTOR_ID, DoctorVisitEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, doctorVisitMapper));
    }

    private RepeatParameters insertRepeatParameters(@NonNull RepeatParameters repeatParameters) {
        return DbUtils.insert(dataStore, repeatParameters, repeatParametersMapper);
    }

    private DoctorVisit insertDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        return DbUtils.insert(dataStore, doctorVisit, doctorVisitMapper);
    }

    private RepeatParameters updateRepeatParameters(@NonNull RepeatParameters repeatParameters) {
        return DbUtils.update(dataStore, repeatParameters, repeatParametersMapper);
    }

    private DoctorVisit updateDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        return DbUtils.update(dataStore, doctorVisit, doctorVisitMapper);
    }

    @Nullable
    private RepeatParameters upsertRepeatParameters(@Nullable RepeatParameters repeatParameters) {
        if (repeatParameters != null) {
            if (repeatParameters.getId() == null) {
                return insertRepeatParameters(repeatParameters);
            } else {
                return updateRepeatParameters(repeatParameters);
            }
        }
        return null;
    }

    public Observable<DoctorVisit> add(@NonNull DoctorVisit doctorVisit) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            RepeatParameters repeatParameters = upsertRepeatParameters(doctorVisit.getRepeatParameters());
            DoctorVisit result = doctorVisit.toBuilder().repeatParameters(repeatParameters).build();
            result = insertDoctorVisit(result);

            boolean needToAddEvents = ObjectUtils.isTrue(result.getIsExported());

            if (needToAddEvents) {
                eventsGenerator.generateEvents(result);
            }

            return result;
        }));
    }

    public Observable<DoctorVisit> update(@NonNull DoctorVisit doctorVisit) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            DoctorVisitEntity oldDoctorVisitEntity = dataStore.toBlocking()
                    .select(DoctorVisitEntity.class)
                    .where(DoctorVisitEntity.ID.eq(doctorVisit.getId()))
                    .get()
                    .first();

            boolean needToAddEvents = ObjectUtils.isFalse(oldDoctorVisitEntity.isExported())
                    && ObjectUtils.isTrue(doctorVisit.getIsExported());

            RepeatParameters repeatParameters = upsertRepeatParameters(doctorVisit.getRepeatParameters());
            DoctorVisit result = doctorVisit.toBuilder().repeatParameters(repeatParameters).build();
            result = updateDoctorVisit(result);

            if (needToAddEvents) {
                eventsGenerator.generateEvents(result);
            }

            return result;
        }));
    }

    public Observable<Integer> deleteDoctorVisitEvents(@NonNull Long doctorVisitId,
                                                       @Nullable Integer linearGroup) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            BlockingEntityStore<Persistable> blockingEntityStore = dataStore.toBlocking();
            val where = blockingEntityStore
                    .select(MasterEventEntity.class)
                    .join(DoctorVisitEventEntity.class).on(MasterEventEntity.ID.eq(DoctorVisitEventEntity.MASTER_EVENT_ID))
                    .where(DoctorVisitEventEntity.DOCTOR_VISIT_ID.eq(doctorVisitId));
            DoctorVisitEntity doctorVisitEntity = blockingEntityStore
                    .findByKey(DoctorVisitEntity.class, doctorVisitId);
            if (linearGroup == null) {
                List<MasterEventEntity> events = where.get().toList();
                int count = events.size();
                deleteManyEvents(blockingEntityStore, events);
                blockingEntityStore.delete(doctorVisitEntity);
                return count;
            } else {
                List<MasterEventEntity> events = where.and(MasterEventEntity.LINEAR_GROUP.eq(linearGroup)).get().toList();
                int count = events.size();
                deleteManyEvents(blockingEntityStore, events);
                deleteIfPossible(blockingEntityStore, doctorVisitEntity);
                return count;
            }
        }));
    }

    public Observable<Integer> completeDoctorVisit(@NonNull Long doctorVisitId,
                                                   @NonNull DateTime dateTime,
                                                   boolean delete) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            BlockingEntityStore<Persistable> blockingEntityStore = dataStore.toBlocking();
            DoctorVisitEntity doctorVisitEntity = blockingEntityStore
                    .findByKey(DoctorVisitEntity.class, doctorVisitId);
            doctorVisitEntity.setFinishDateTime(dateTime);
            blockingEntityStore.update(doctorVisitEntity);
            int count = 0;
            if (delete) {
                List<MasterEventEntity> events = blockingEntityStore
                        .select(MasterEventEntity.class)
                        .join(DoctorVisitEventEntity.class).on(MasterEventEntity.ID.eq(DoctorVisitEventEntity.MASTER_EVENT_ID))
                        .where(DoctorVisitEventEntity.DOCTOR_VISIT_ID.eq(doctorVisitId))
                        .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(dateTime.toDateTime()))
                        .get()
                        .toList();
                count = events.size();
                deleteManyEvents(blockingEntityStore, events);
            }
            return count;
        }));
    }

    public Observable<DoctorVisit> delete(@NonNull DoctorVisit doctorVisit) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            BlockingEntityStore<Persistable> blockingEntityStore = dataStore.toBlocking();
            List<MasterEventEntity> events = blockingEntityStore
                    .select(MasterEventEntity.class)
                    .join(DoctorVisitEventEntity.class).on(MasterEventEntity.ID.eq(DoctorVisitEventEntity.MASTER_EVENT_ID))
                    .where(DoctorVisitEventEntity.DOCTOR_VISIT_ID.eq(doctorVisit.getId()))
                    .get()
                    .toList();
            int count = events.size();
            DoctorVisitEntity doctorVisitEntity = blockingEntityStore
                    .findByKey(DoctorVisitEntity.class, doctorVisit.getId());
            if (count == 0) {
                blockingEntityStore.delete(doctorVisitEntity);
                logger.debug("doctor visit deleted hardly");
            } else {
                doctorVisitEntity.setDeleted(true);
                blockingEntityStore.update(doctorVisitEntity);
                logger.debug("doctor visit deleted softly");
            }
            return doctorVisitMapper.mapToPlainObject(doctorVisitEntity);
        }));
    }

    private void deleteIfPossible(BlockingEntityStore<Persistable> blockingEntityStore,
                                  DoctorVisitEntity doctorVisitEntity) {
        if (ObjectUtils.isTrue(doctorVisitEntity.isDeleted())) {
            List<MasterEventEntity> events = blockingEntityStore
                    .select(MasterEventEntity.class)
                    .join(DoctorVisitEventEntity.class).on(MasterEventEntity.ID.eq(DoctorVisitEventEntity.MASTER_EVENT_ID))
                    .where(DoctorVisitEventEntity.DOCTOR_VISIT_ID.eq(doctorVisitEntity.getId()))
                    .get()
                    .toList();
            if (events.isEmpty()) {
                blockingEntityStore.delete(doctorVisitEntity);
                logger.debug("doctor visit deleted hardly");
            }
        }
    }

    private void deleteManyEvents(BlockingEntityStore<Persistable> blockingEntityStore,
                                  List<MasterEventEntity> events) {
        final int MAX = 10;
        for (int i = 0; i < events.size(); i += MAX) {
            int upper = Math.min(i + MAX, events.size());
            val subList = events.subList(i, upper);
            blockingEntityStore.delete(subList);
        }
    }
}
