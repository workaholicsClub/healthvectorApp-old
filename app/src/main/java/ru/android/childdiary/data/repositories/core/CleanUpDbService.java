package ru.android.childdiary.data.repositories.core;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.query.WhereAndOr;
import io.requery.reactivex.ReactiveEntityStore;
import lombok.val;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.entities.calendar.events.DoctorVisitEventEntity;
import ru.android.childdiary.data.entities.calendar.events.MedicineTakingEventEntity;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.data.entities.medical.DoctorVisitEntity;
import ru.android.childdiary.data.entities.medical.MedicineTakingEntity;
import ru.android.childdiary.data.repositories.medical.mappers.DoctorVisitMapper;
import ru.android.childdiary.data.repositories.medical.mappers.MedicineTakingMapper;
import ru.android.childdiary.domain.interactors.calendar.events.DoctorVisitEvent;
import ru.android.childdiary.domain.interactors.calendar.events.MedicineTakingEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.requests.DeleteChildRequest;
import ru.android.childdiary.domain.interactors.child.requests.DeleteChildResponse;
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
    private final BlockingEntityStore<Persistable> blockingEntityStore;
    private final DoctorVisitMapper doctorVisitMapper;
    private final MedicineTakingMapper medicineTakingMapper;

    @Inject
    public CleanUpDbService(ReactiveEntityStore<Persistable> dataStore,
                            DoctorVisitMapper doctorVisitMapper,
                            MedicineTakingMapper medicineTakingMapper) {
        this.dataStore = dataStore;
        this.blockingEntityStore = dataStore.toBlocking();
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

    public Observable<DeleteChildResponse> deleteChild(@NonNull DeleteChildRequest request) {
        return Observable.fromCallable(() -> {
            Child child = request.getChild();
            List<String> imageFilesToDelete = new ArrayList<>();

            List<DoctorVisitEntity> doctorVisitEntities = blockingEntityStore.select(DoctorVisitEntity.class)
                    .where(DoctorVisitEntity.CHILD_ID.eq(child.getId()))
                    .get().toList();
            imageFilesToDelete.addAll(getImageFileNamesDoctorVisit(doctorVisitEntities));

            List<MedicineTakingEntity> medicineTakingEntities = blockingEntityStore.select(MedicineTakingEntity.class)
                    .where(MedicineTakingEntity.CHILD_ID.eq(child.getId()))
                    .get().toList();
            imageFilesToDelete.addAll(getImageFileNamesMedicineTaking(medicineTakingEntities));

            List<DoctorVisitEventEntity> doctorVisitEventEntities = blockingEntityStore.select(DoctorVisitEventEntity.class)
                    .join(MasterEventEntity.class).on(MasterEventEntity.ID.eq(DoctorVisitEventEntity.MASTER_EVENT_ID))
                    .where(MasterEventEntity.CHILD_ID.eq(child.getId()))
                    .get().toList();
            imageFilesToDelete.addAll(getImageFileNamesDoctorVisitEvents(doctorVisitEventEntities));

            List<MedicineTakingEventEntity> medicineTakingEventEntities = blockingEntityStore.select(MedicineTakingEventEntity.class)
                    .join(MasterEventEntity.class).on(MasterEventEntity.ID.eq(MedicineTakingEventEntity.MASTER_EVENT_ID))
                    .where(MasterEventEntity.CHILD_ID.eq(child.getId()))
                    .get().toList();
            imageFilesToDelete.addAll(getImageFileNamesMedicineTakingEvents(medicineTakingEventEntities));

            ChildEntity childEntity = blockingEntityStore.findByKey(ChildEntity.class, child.getId());
            imageFilesToDelete.add(childEntity.getImageFileName());
            blockingEntityStore.delete(childEntity);

            return DeleteChildResponse.builder()
                    .request(request)
                    .imageFilesToDelete(imageFilesToDelete)
                    .build();
        });
    }

    public Observable<DeleteDoctorVisitEventsResponse> deleteDoctorVisitEvents(
            @NonNull DeleteDoctorVisitEventsRequest request) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            Long id = getId(request.getDoctorVisit());
            DoctorVisitEntity doctorVisitEntity = findDoctorVisitEntity(id);
            val events = request.getLinearGroup() == null
                    ? getDoctorVisitEvents(id)
                    : getDoctorVisitEvents(id, request.getLinearGroup());
            int count = events.size();
            List<String> imageFilesToDelete = new ArrayList<>();
            imageFilesToDelete.addAll(getImageFileNamesDoctorVisitEvents(events));
            deleteDoctorVisitEvents(events);
            if (request.getLinearGroup() == null) {
                delete(doctorVisitEntity, imageFilesToDelete);
            } else {
                deleteIfPossible(doctorVisitEntity, imageFilesToDelete);
            }
            return DeleteDoctorVisitEventsResponse.builder()
                    .request(request)
                    .count(count)
                    .imageFilesToDelete(imageFilesToDelete)
                    .build();
        }));
    }

    public Observable<DeleteMedicineTakingEventsResponse> deleteMedicineTakingEvents(
            @NonNull DeleteMedicineTakingEventsRequest request) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            Long id = getId(request.getMedicineTaking());
            MedicineTakingEntity medicineTakingEntity = findMedicineTakingEntity(id);
            val events = request.getLinearGroup() == null
                    ? getMedicineTakingEvents(id)
                    : getMedicineTakingEvents(id, request.getLinearGroup());
            int count = events.size();
            List<String> imageFilesToDelete = new ArrayList<>();
            imageFilesToDelete.addAll(getImageFileNamesMedicineTakingEvents(events));
            deleteMedicineTakingEvents(events);
            if (request.getLinearGroup() == null) {
                delete(medicineTakingEntity, imageFilesToDelete);
            } else {
                deleteIfPossible(medicineTakingEntity, imageFilesToDelete);
            }
            return DeleteMedicineTakingEventsResponse.builder()
                    .request(request)
                    .count(count)
                    .imageFilesToDelete(imageFilesToDelete)
                    .build();
        }));
    }

    public Observable<CompleteDoctorVisitResponse> completeDoctorVisit(@NonNull CompleteDoctorVisitRequest request) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            Long id = getId(request.getDoctorVisit());
            DoctorVisitEntity doctorVisitEntity = findDoctorVisitEntity(id);
            doctorVisitEntity.setFinishDateTime(request.getDateTime());
            blockingEntityStore.update(doctorVisitEntity);
            int count = 0;
            List<String> imageFilesToDelete = new ArrayList<>();
            if (request.isDelete()) {
                val events = getDoctorVisitEvents(id, request.getDateTime());
                count = events.size();
                imageFilesToDelete.addAll(getImageFileNamesDoctorVisitEvents(events));
                deleteDoctorVisitEvents(events);
            }
            return CompleteDoctorVisitResponse.builder()
                    .request(request)
                    .count(count)
                    .imageFilesToDelete(imageFilesToDelete)
                    .build();
        }));
    }

    public Observable<CompleteMedicineTakingResponse> completeMedicineTaking(@NonNull CompleteMedicineTakingRequest request) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            Long id = getId(request.getMedicineTaking());
            MedicineTakingEntity medicineTakingEntity = findMedicineTakingEntity(id);
            medicineTakingEntity.setFinishDateTime(request.getDateTime());
            blockingEntityStore.update(medicineTakingEntity);
            int count = 0;
            List<String> imageFilesToDelete = new ArrayList<>();
            if (request.isDelete()) {
                val events = getMedicineTakingEvents(id, request.getDateTime());
                count = events.size();
                imageFilesToDelete.addAll(getImageFileNamesMedicineTakingEvents(events));
                deleteMedicineTakingEvents(events);
            }
            return CompleteMedicineTakingResponse.builder()
                    .request(request)
                    .count(count)
                    .imageFilesToDelete(imageFilesToDelete)
                    .build();
        }));
    }

    public Observable<DeleteDoctorVisitResponse> deleteDoctorVisit(@NonNull DeleteDoctorVisitRequest request) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            Long id = getId(request.getDoctorVisit());
            val events = getDoctorVisitEvents(id);
            int count = events.size();
            List<String> imageFilesToDelete = new ArrayList<>();
            imageFilesToDelete.addAll(getImageFileNamesDoctorVisitEvents(events));
            DoctorVisitEntity doctorVisitEntity = findDoctorVisitEntity(id);
            if (count == 0) {
                delete(doctorVisitEntity, imageFilesToDelete);
            } else {
                deleteSoftly(doctorVisitEntity);
            }
            return DeleteDoctorVisitResponse.builder()
                    .request(request)
                    .imageFilesToDelete(imageFilesToDelete)
                    .build();
        }));
    }

    public Observable<DeleteMedicineTakingResponse> deleteMedicineTaking(@NonNull DeleteMedicineTakingRequest request) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            Long id = getId(request.getMedicineTaking());
            val events = getMedicineTakingEvents(id);
            int count = events.size();
            List<String> imageFilesToDelete = new ArrayList<>();
            imageFilesToDelete.addAll(getImageFileNamesMedicineTakingEvents(events));
            MedicineTakingEntity medicineTakingEntity = findMedicineTakingEntity(id);
            if (count == 0) {
                delete(medicineTakingEntity, imageFilesToDelete);
            } else {
                deleteSoftly(medicineTakingEntity);
            }
            return DeleteMedicineTakingResponse.builder()
                    .request(request)
                    .imageFilesToDelete(imageFilesToDelete)
                    .build();
        }));
    }

    public <T extends MasterEvent> Observable<List<String>> deleteEvent(@NonNull T event) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            List<String> imageFilesToDelete = new ArrayList<>();
            DbUtils.delete(blockingEntityStore, MasterEventEntity.class, event, event.getMasterEventId());
            if (event instanceof DoctorVisitEvent) {
                DoctorVisitEvent doctorVisitEvent = (DoctorVisitEvent) event;
                imageFilesToDelete.add(doctorVisitEvent.getImageFileName());
                Long id = getId(doctorVisitEvent.getDoctorVisit());
                DoctorVisitEntity doctorVisitEntity = findDoctorVisitEntity(id);
                deleteIfPossible(doctorVisitEntity, imageFilesToDelete);
            } else if (event instanceof MedicineTakingEvent) {
                MedicineTakingEvent medicineTakingEvent = (MedicineTakingEvent) event;
                imageFilesToDelete.add(medicineTakingEvent.getImageFileName());
                Long id = getId(medicineTakingEvent.getMedicineTaking());
                MedicineTakingEntity medicineTakingEntity = findMedicineTakingEntity(id);
                deleteIfPossible(medicineTakingEntity, imageFilesToDelete);
            }
            return imageFilesToDelete;
        }));
    }

    private DoctorVisitEntity findDoctorVisitEntity(Long id) {
        return blockingEntityStore.findByKey(DoctorVisitEntity.class, id);
    }

    private MedicineTakingEntity findMedicineTakingEntity(Long id) {
        return blockingEntityStore.findByKey(MedicineTakingEntity.class, id);
    }

    private void delete(DoctorVisitEntity doctorVisitEntity,
                        List<String> imageFilesToDelete) {
        imageFilesToDelete.add(doctorVisitEntity.getImageFileName());
        blockingEntityStore.delete(doctorVisitEntity);
        logger.debug("doctor visit deleted");
    }

    private void deleteSoftly(DoctorVisitEntity doctorVisitEntity) {
        doctorVisitEntity.setDeleted(true);
        blockingEntityStore.update(doctorVisitEntity);
        logger.debug("doctor visit deleted softly");
    }

    private void deleteIfPossible(DoctorVisitEntity doctorVisitEntity,
                                  List<String> imageFilesToDelete) {
        if (ObjectUtils.isTrue(doctorVisitEntity.isDeleted())) {
            val events = getDoctorVisitEvents(doctorVisitEntity.getId());
            if (events.isEmpty()) {
                delete(doctorVisitEntity, imageFilesToDelete);
            }
        }
    }

    private void delete(MedicineTakingEntity medicineTakingEntity,
                        List<String> imageFilesToDelete) {
        imageFilesToDelete.add(medicineTakingEntity.getImageFileName());
        blockingEntityStore.delete(medicineTakingEntity);
        logger.debug("medicine taking deleted");
    }

    private void deleteSoftly(MedicineTakingEntity medicineTakingEntity) {
        medicineTakingEntity.setDeleted(true);
        blockingEntityStore.update(medicineTakingEntity);
        logger.debug("medicine taking deleted softly");
    }

    private void deleteIfPossible(MedicineTakingEntity medicineTakingEntity,
                                  List<String> imageFilesToDelete) {
        if (ObjectUtils.isTrue(medicineTakingEntity.isDeleted())) {
            val events = getMedicineTakingEvents(medicineTakingEntity.getId());
            if (events.isEmpty()) {
                delete(medicineTakingEntity, imageFilesToDelete);
            }
        }
    }

    private void deleteDoctorVisitEvents(List<DoctorVisitEventEntity> events) {
        val masterEvents = Observable.fromIterable(events)
                .map(event -> blockingEntityStore.findByKey(MasterEventEntity.class,
                        event.getMasterEvent().getId()))
                .toList().blockingGet();
        delete(masterEvents);
    }

    private void deleteMedicineTakingEvents(List<MedicineTakingEventEntity> events) {
        val masterEvents = Observable.fromIterable(events)
                .map(event -> blockingEntityStore.findByKey(MasterEventEntity.class,
                        event.getMasterEvent().getId()))
                .toList().blockingGet();
        delete(masterEvents);
    }

    private <T extends Persistable> void delete(List<T> entities) {
        final int MAX = 10;
        for (int i = 0; i < entities.size(); i += MAX) {
            int upper = Math.min(i + MAX, entities.size());
            val subList = entities.subList(i, upper);
            blockingEntityStore.delete(subList);
        }
    }

    private List<String> getImageFileNamesDoctorVisitEvents(List<DoctorVisitEventEntity> entities) {
        return Observable.fromIterable(entities)
                .filter(event -> !TextUtils.isEmpty(event.getImageFileName()))
                .map(DoctorVisitEventEntity::getImageFileName)
                .toList()
                .blockingGet();
    }

    private List<String> getImageFileNamesMedicineTakingEvents(List<MedicineTakingEventEntity> entities) {
        return Observable.fromIterable(entities)
                .filter(event -> !TextUtils.isEmpty(event.getImageFileName()))
                .map(MedicineTakingEventEntity::getImageFileName)
                .toList()
                .blockingGet();
    }

    private List<String> getImageFileNamesDoctorVisit(List<DoctorVisitEntity> entities) {
        return Observable.fromIterable(entities)
                .filter(event -> !TextUtils.isEmpty(event.getImageFileName()))
                .map(DoctorVisitEntity::getImageFileName)
                .toList()
                .blockingGet();
    }

    private List<String> getImageFileNamesMedicineTaking(List<MedicineTakingEntity> entities) {
        return Observable.fromIterable(entities)
                .filter(event -> !TextUtils.isEmpty(event.getImageFileName()))
                .map(MedicineTakingEntity::getImageFileName)
                .toList()
                .blockingGet();
    }

    private List<DoctorVisitEventEntity> getDoctorVisitEvents(Long id) {
        return selectDoctorVisitEvents(id).get().toList();
    }

    private List<DoctorVisitEventEntity> getDoctorVisitEvents(Long id, Integer linearGroup) {
        return selectDoctorVisitEvents(id)
                .and(MasterEventEntity.LINEAR_GROUP.eq(linearGroup))
                .get().toList();
    }

    private List<DoctorVisitEventEntity> getDoctorVisitEvents(Long id, DateTime dateTime) {
        return selectDoctorVisitEvents(id)
                .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(dateTime.toDateTime()))
                .get().toList();
    }

    private List<MedicineTakingEventEntity> getMedicineTakingEvents(Long id) {
        return selectMedicineTakingEvents(id).get().toList();
    }

    private List<MedicineTakingEventEntity> getMedicineTakingEvents(Long id, Integer linearGroup) {
        return selectMedicineTakingEvents(id)
                .and(MasterEventEntity.LINEAR_GROUP.eq(linearGroup))
                .get().toList();
    }

    private List<MedicineTakingEventEntity> getMedicineTakingEvents(Long id, DateTime dateTime) {
        return selectMedicineTakingEvents(id)
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
}
