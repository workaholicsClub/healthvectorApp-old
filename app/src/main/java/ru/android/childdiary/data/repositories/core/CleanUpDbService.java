package ru.android.childdiary.data.repositories.core;

import android.support.annotation.NonNull;

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
import ru.android.childdiary.data.entities.calendar.events.DoctorVisitEventEntity;
import ru.android.childdiary.data.entities.calendar.events.MedicineTakingEventEntity;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.entities.medical.DoctorVisitEntity;
import ru.android.childdiary.data.entities.medical.MedicineTakingEntity;
import ru.android.childdiary.data.repositories.medical.mappers.DoctorVisitMapper;
import ru.android.childdiary.data.repositories.medical.mappers.MedicineTakingMapper;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.requests.CompleteDoctorVisitRequest;
import ru.android.childdiary.domain.interactors.medical.requests.CompleteDoctorVisitResponse;
import ru.android.childdiary.domain.interactors.medical.requests.CompleteMedicineTakingRequest;
import ru.android.childdiary.domain.interactors.medical.requests.CompleteMedicineTakingResponse;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteDoctorVisitEventsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteDoctorVisitEventsResponse;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteDoctorVisitRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteDoctorVisitResponse;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteMedicineTakingEventsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteMedicineTakingEventsResponse;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteMedicineTakingRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteMedicineTakingResponse;
import ru.android.childdiary.utils.ObjectUtils;

@Singleton
public class CleanUpDbService {
    private final Logger logger = LoggerFactory.getLogger(toString());
    private final ReactiveEntityStore<Persistable> dataStore;
    private final DoctorVisitMapper doctorVisitMapper;
    private final MedicineTakingMapper medicineTakingMapper;

    @Inject
    public CleanUpDbService(ReactiveEntityStore<Persistable> dataStore,
                            DoctorVisitMapper doctorVisitMapper,
                            MedicineTakingMapper medicineTakingMapper) {
        this.dataStore = dataStore;
        this.doctorVisitMapper = doctorVisitMapper;
        this.medicineTakingMapper = medicineTakingMapper;
    }

    private Long getId(@NonNull DoctorVisit doctorVisit) {
        Long id = doctorVisit.getId();
        if (id == null) {
            throw new IllegalStateException("Doctor visit id is null");
        }
        return id;
    }

    private Long getId(@NonNull MedicineTaking medicineTaking) {
        Long id = medicineTaking.getId();
        if (id == null) {
            throw new IllegalStateException("Medicine taking id is null");
        }
        return id;
    }

    public Observable<DeleteDoctorVisitEventsResponse> deleteDoctorVisitEvents(
            @NonNull DeleteDoctorVisitEventsRequest request) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            Long id = getId(request.getDoctorVisit());
            BlockingEntityStore<Persistable> blockingEntityStore = dataStore.toBlocking();
            val where = blockingEntityStore
                    .select(MasterEventEntity.class)
                    .join(DoctorVisitEventEntity.class).on(MasterEventEntity.ID.eq(DoctorVisitEventEntity.MASTER_EVENT_ID))
                    .where(DoctorVisitEventEntity.DOCTOR_VISIT_ID.eq(id));
            DoctorVisitEntity doctorVisitEntity = blockingEntityStore
                    .findByKey(DoctorVisitEntity.class, id);
            int count;
            if (request.getLinearGroup() == null) {
                List<MasterEventEntity> events = where
                        .get()
                        .toList();
                count = events.size();
                deleteManyEvents(blockingEntityStore, events);
                blockingEntityStore.delete(doctorVisitEntity);
            } else {
                List<MasterEventEntity> events = where
                        .and(MasterEventEntity.LINEAR_GROUP.eq(request.getLinearGroup()))
                        .get()
                        .toList();
                count = events.size();
                deleteManyEvents(blockingEntityStore, events);
                deleteIfPossible(blockingEntityStore, doctorVisitEntity);
            }
            return DeleteDoctorVisitEventsResponse.builder()
                    .request(request)
                    .count(count)
                    .imageFilesToDelete(null)//TODO
                    .build();
        }));
    }

    public Observable<DeleteMedicineTakingEventsResponse> deleteMedicineTakingEvents(
            @NonNull DeleteMedicineTakingEventsRequest request) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            Long id = getId(request.getMedicineTaking());
            BlockingEntityStore<Persistable> blockingEntityStore = dataStore.toBlocking();
            val where = blockingEntityStore
                    .select(MasterEventEntity.class)
                    .join(MedicineTakingEventEntity.class).on(MasterEventEntity.ID.eq(MedicineTakingEventEntity.MASTER_EVENT_ID))
                    .where(MedicineTakingEventEntity.MEDICINE_TAKING_ID.eq(id));
            MedicineTakingEntity medicineTakingEntity = blockingEntityStore
                    .findByKey(MedicineTakingEntity.class, id);
            int count;
            if (request.getLinearGroup() == null) {
                List<MasterEventEntity> events = where
                        .get()
                        .toList();
                count = events.size();
                deleteManyEvents(blockingEntityStore, events);
                blockingEntityStore.delete(medicineTakingEntity);
            } else {
                List<MasterEventEntity> events = where
                        .and(MasterEventEntity.LINEAR_GROUP.eq(request.getLinearGroup()))
                        .get()
                        .toList();
                count = events.size();
                deleteManyEvents(blockingEntityStore, events);
                deleteIfPossible(blockingEntityStore, medicineTakingEntity);
            }
            return DeleteMedicineTakingEventsResponse.builder()
                    .request(request)
                    .count(count)
                    .imageFilesToDelete(null)//TODO
                    .build();
        }));
    }

    public Observable<CompleteDoctorVisitResponse> completeDoctorVisit(@NonNull CompleteDoctorVisitRequest request) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            Long id = getId(request.getDoctorVisit());
            BlockingEntityStore<Persistable> blockingEntityStore = dataStore.toBlocking();
            DoctorVisitEntity doctorVisitEntity = blockingEntityStore
                    .findByKey(DoctorVisitEntity.class, id);
            doctorVisitEntity.setFinishDateTime(request.getDateTime());
            blockingEntityStore.update(doctorVisitEntity);
            int count = 0;
            if (request.isDelete()) {
                List<MasterEventEntity> events = blockingEntityStore
                        .select(MasterEventEntity.class)
                        .join(DoctorVisitEventEntity.class).on(MasterEventEntity.ID.eq(DoctorVisitEventEntity.MASTER_EVENT_ID))
                        .where(DoctorVisitEventEntity.DOCTOR_VISIT_ID.eq(id))
                        .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(request.getDateTime().toDateTime()))
                        .get()
                        .toList();
                count = events.size();
                deleteManyEvents(blockingEntityStore, events);
            }
            return CompleteDoctorVisitResponse.builder()
                    .request(request)
                    .count(count)
                    .imageFilesToDelete(null)//TODO
                    .build();
        }));
    }

    public Observable<CompleteMedicineTakingResponse> completeMedicineTaking(@NonNull CompleteMedicineTakingRequest request) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            Long id = getId(request.getMedicineTaking());
            BlockingEntityStore<Persistable> blockingEntityStore = dataStore.toBlocking();
            MedicineTakingEntity medicineTakingEntity = blockingEntityStore
                    .findByKey(MedicineTakingEntity.class, id);
            medicineTakingEntity.setFinishDateTime(request.getDateTime());
            blockingEntityStore.update(medicineTakingEntity);
            int count = 0;
            if (request.isDelete()) {
                List<MasterEventEntity> events = blockingEntityStore
                        .select(MasterEventEntity.class)
                        .join(MedicineTakingEventEntity.class).on(MasterEventEntity.ID.eq(MedicineTakingEventEntity.MASTER_EVENT_ID))
                        .where(MedicineTakingEventEntity.MEDICINE_TAKING_ID.eq(id))
                        .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(request.getDateTime().toDateTime()))
                        .get()
                        .toList();
                count = events.size();
                deleteManyEvents(blockingEntityStore, events);
            }
            return CompleteMedicineTakingResponse.builder()
                    .request(request)
                    .count(count)
                    .imageFilesToDelete(null)//TODO
                    .build();
        }));
    }

    public Observable<DeleteDoctorVisitResponse> deleteDoctorVisit(@NonNull DeleteDoctorVisitRequest request) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            Long id = getId(request.getDoctorVisit());
            BlockingEntityStore<Persistable> blockingEntityStore = dataStore.toBlocking();
            List<MasterEventEntity> events = blockingEntityStore
                    .select(MasterEventEntity.class)
                    .join(DoctorVisitEventEntity.class).on(MasterEventEntity.ID.eq(DoctorVisitEventEntity.MASTER_EVENT_ID))
                    .where(DoctorVisitEventEntity.DOCTOR_VISIT_ID.eq(id))
                    .get()
                    .toList();
            int count = events.size();
            DoctorVisitEntity doctorVisitEntity = blockingEntityStore
                    .findByKey(DoctorVisitEntity.class, id);
            if (count == 0) {
                blockingEntityStore.delete(doctorVisitEntity);
                logger.debug("doctor visit deleted hardly");
            } else {
                doctorVisitEntity.setDeleted(true);
                blockingEntityStore.update(doctorVisitEntity);
                logger.debug("doctor visit deleted softly");
            }
            return DeleteDoctorVisitResponse.builder()
                    .request(request)
                    .imageFilesToDelete(null)// TODO
                    .build();
        }));
    }

    public Observable<DeleteMedicineTakingResponse> deleteMedicineTaking(@NonNull DeleteMedicineTakingRequest request) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            Long id = getId(request.getMedicineTaking());
            BlockingEntityStore<Persistable> blockingEntityStore = dataStore.toBlocking();
            List<MasterEventEntity> events = blockingEntityStore
                    .select(MasterEventEntity.class)
                    .join(MedicineTakingEventEntity.class).on(MasterEventEntity.ID.eq(MedicineTakingEventEntity.MASTER_EVENT_ID))
                    .where(MedicineTakingEventEntity.MEDICINE_TAKING_ID.eq(id))
                    .get()
                    .toList();
            int count = events.size();
            MedicineTakingEntity medicineTakingEntity = blockingEntityStore
                    .findByKey(MedicineTakingEntity.class, id);
            if (count == 0) {
                blockingEntityStore.delete(medicineTakingEntity);
                logger.debug("medicine taking deleted hardly");
            } else {
                medicineTakingEntity.setDeleted(true);
                blockingEntityStore.update(medicineTakingEntity);
                logger.debug("medicine taking deleted softly");
            }
            return DeleteMedicineTakingResponse.builder()
                    .request(request)
                    .imageFilesToDelete(null)// TODO
                    .build();
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

    private void deleteIfPossible(BlockingEntityStore<Persistable> blockingEntityStore,
                                  MedicineTakingEntity medicineTakingEntity) {
        if (ObjectUtils.isTrue(medicineTakingEntity.isDeleted())) {
            List<MasterEventEntity> events = blockingEntityStore
                    .select(MasterEventEntity.class)
                    .join(MedicineTakingEventEntity.class).on(MasterEventEntity.ID.eq(MedicineTakingEventEntity.MASTER_EVENT_ID))
                    .where(MedicineTakingEventEntity.MEDICINE_TAKING_ID.eq(medicineTakingEntity.getId()))
                    .get()
                    .toList();
            if (events.isEmpty()) {
                blockingEntityStore.delete(medicineTakingEntity);
                logger.debug("medicine taking deleted hardly");
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
