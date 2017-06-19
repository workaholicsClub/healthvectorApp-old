package ru.android.childdiary.data.repositories.core;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import lombok.val;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.entities.calendar.events.DoctorVisitEventEntity;
import ru.android.childdiary.data.entities.calendar.events.ExerciseEventEntity;
import ru.android.childdiary.data.entities.calendar.events.MedicineTakingEventEntity;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.data.entities.exercises.ConcreteExerciseEntity;
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
import ru.android.childdiary.domain.interactors.exercises.requests.DeleteConcreteExerciseEventsRequest;
import ru.android.childdiary.domain.interactors.exercises.requests.DeleteConcreteExerciseEventsResponse;
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
public class CleanUpDbService extends EventsDbService {
    private final DoctorVisitMapper doctorVisitMapper;
    private final MedicineTakingMapper medicineTakingMapper;

    @Inject
    public CleanUpDbService(ReactiveEntityStore<Persistable> dataStore,
                            DoctorVisitMapper doctorVisitMapper,
                            MedicineTakingMapper medicineTakingMapper) {
        super(dataStore);
        this.doctorVisitMapper = doctorVisitMapper;
        this.medicineTakingMapper = medicineTakingMapper;
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
            Long id = getDoctorVisitId(request.getDoctorVisit());
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
            Long id = getMedicineTakingId(request.getMedicineTaking());
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

    public Observable<DeleteConcreteExerciseEventsResponse> deleteExerciseEvents(
            @NonNull DeleteConcreteExerciseEventsRequest request) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            Long id = getConcreteExerciseId(request.getConcreteExercise());
            ConcreteExerciseEntity concreteExerciseEntity = findConcreteExerciseEntity(id);
            val events = request.getLinearGroup() == null
                    ? getExerciseEvents(id)
                    : getExerciseEvents(id, request.getLinearGroup());
            int count = events.size();
            List<String> imageFilesToDelete = new ArrayList<>();
            imageFilesToDelete.addAll(getImageFileNamesExerciseEvents(events));
            deleteExerciseEvents(events);
            if (request.getLinearGroup() == null) {
                delete(concreteExerciseEntity, imageFilesToDelete);
            } else {
                deleteIfPossible(concreteExerciseEntity, imageFilesToDelete);
            }
            return DeleteConcreteExerciseEventsResponse.builder()
                    .request(request)
                    .count(count)
                    .imageFilesToDelete(imageFilesToDelete)
                    .build();
        }));
    }

    public Observable<CompleteDoctorVisitResponse> completeDoctorVisit(@NonNull CompleteDoctorVisitRequest request) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            Long id = getDoctorVisitId(request.getDoctorVisit());
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
            Long id = getMedicineTakingId(request.getMedicineTaking());
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
            Long id = getDoctorVisitId(request.getDoctorVisit());
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
            Long id = getMedicineTakingId(request.getMedicineTaking());
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
                Long id = getDoctorVisitId(doctorVisitEvent.getDoctorVisit());
                DoctorVisitEntity doctorVisitEntity = findDoctorVisitEntity(id);
                deleteIfPossible(doctorVisitEntity, imageFilesToDelete);
            } else if (event instanceof MedicineTakingEvent) {
                MedicineTakingEvent medicineTakingEvent = (MedicineTakingEvent) event;
                imageFilesToDelete.add(medicineTakingEvent.getImageFileName());
                Long id = getMedicineTakingId(medicineTakingEvent.getMedicineTaking());
                MedicineTakingEntity medicineTakingEntity = findMedicineTakingEntity(id);
                deleteIfPossible(medicineTakingEntity, imageFilesToDelete);
            }
            return imageFilesToDelete;
        }));
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

    private void delete(ConcreteExerciseEntity concreteExerciseEntity,
                        List<String> imageFilesToDelete) {
        imageFilesToDelete.add(concreteExerciseEntity.getImageFileName());
        blockingEntityStore.delete(concreteExerciseEntity);
        logger.debug("concrete exercise deleted");
    }

    private void deleteSoftly(ConcreteExerciseEntity concreteExerciseEntity) {
        concreteExerciseEntity.setDeleted(true);
        blockingEntityStore.update(concreteExerciseEntity);
        logger.debug("concrete exercise deleted softly");
    }

    private void deleteIfPossible(ConcreteExerciseEntity concreteExerciseEntity,
                                  List<String> imageFilesToDelete) {
        if (ObjectUtils.isTrue(concreteExerciseEntity.isDeleted())) {
            val events = getExerciseEvents(concreteExerciseEntity.getId());
            if (events.isEmpty()) {
                delete(concreteExerciseEntity, imageFilesToDelete);
            }
        }
    }

    private void deleteDoctorVisitEvents(List<DoctorVisitEventEntity> events) {
        val masterEvents = getMasterEventsFromDoctorVisitEvents(events);
        delete(masterEvents);
    }

    private void deleteMedicineTakingEvents(List<MedicineTakingEventEntity> events) {
        val masterEvents = getMasterEventsFromMedicineTakingEvents(events);
        delete(masterEvents);
    }

    private void deleteExerciseEvents(List<ExerciseEventEntity> events) {
        val masterEvents = getMasterEventsFromExerciseEvents(events);
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

    private List<String> getImageFileNamesExerciseEvents(List<ExerciseEventEntity> entities) {
        return Observable.fromIterable(entities)
                .filter(event -> !TextUtils.isEmpty(event.getImageFileName()))
                .map(ExerciseEventEntity::getImageFileName)
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

    private List<String> getImageFileNamesConcreteExercise(List<ConcreteExerciseEntity> entities) {
        return Observable.fromIterable(entities)
                .filter(event -> !TextUtils.isEmpty(event.getImageFileName()))
                .map(ConcreteExerciseEntity::getImageFileName)
                .toList()
                .blockingGet();
    }
}
