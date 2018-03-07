package ru.android.healthvector.data.repositories.calendar;

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
import ru.android.healthvector.data.db.DbUtils;
import ru.android.healthvector.data.db.entities.calendar.DoctorVisitEventEntity;
import ru.android.healthvector.data.db.entities.calendar.ExerciseEventEntity;
import ru.android.healthvector.data.db.entities.calendar.MedicineTakingEventEntity;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventEntity;
import ru.android.healthvector.data.db.entities.child.ChildEntity;
import ru.android.healthvector.data.db.entities.exercises.ConcreteExerciseEntity;
import ru.android.healthvector.data.db.entities.medical.DoctorVisitEntity;
import ru.android.healthvector.data.db.entities.medical.MedicineTakingEntity;
import ru.android.healthvector.data.repositories.medical.mappers.DoctorVisitMapper;
import ru.android.healthvector.data.repositories.medical.mappers.MedicineTakingMapper;
import ru.android.healthvector.domain.calendar.data.DoctorVisitEvent;
import ru.android.healthvector.domain.calendar.data.ExerciseEvent;
import ru.android.healthvector.domain.calendar.data.MedicineTakingEvent;
import ru.android.healthvector.domain.calendar.data.core.MasterEvent;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.child.requests.DeleteChildRequest;
import ru.android.healthvector.domain.child.requests.DeleteChildResponse;
import ru.android.healthvector.domain.exercises.requests.DeleteConcreteExerciseEventsRequest;
import ru.android.healthvector.domain.exercises.requests.DeleteConcreteExerciseEventsResponse;
import ru.android.healthvector.domain.medical.data.DoctorVisit;
import ru.android.healthvector.domain.medical.data.MedicineTaking;
import ru.android.healthvector.domain.medical.requests.CompleteDoctorVisitRequest;
import ru.android.healthvector.domain.medical.requests.CompleteDoctorVisitResponse;
import ru.android.healthvector.domain.medical.requests.CompleteMedicineTakingRequest;
import ru.android.healthvector.domain.medical.requests.CompleteMedicineTakingResponse;
import ru.android.healthvector.domain.medical.requests.DeleteDoctorVisitEventsRequest;
import ru.android.healthvector.domain.medical.requests.DeleteDoctorVisitEventsResponse;
import ru.android.healthvector.domain.medical.requests.DeleteDoctorVisitRequest;
import ru.android.healthvector.domain.medical.requests.DeleteDoctorVisitResponse;
import ru.android.healthvector.domain.medical.requests.DeleteMedicineTakingEventsRequest;
import ru.android.healthvector.domain.medical.requests.DeleteMedicineTakingEventsResponse;
import ru.android.healthvector.domain.medical.requests.DeleteMedicineTakingRequest;
import ru.android.healthvector.domain.medical.requests.DeleteMedicineTakingResponse;
import ru.android.healthvector.utils.ObjectUtils;

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
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
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

            List<ConcreteExerciseEntity> concreteExerciseEntities = blockingEntityStore.select(ConcreteExerciseEntity.class)
                    .where(ConcreteExerciseEntity.CHILD_ID.eq(child.getId()))
                    .get().toList();
            imageFilesToDelete.addAll(getImageFileNamesConcreteExercise(concreteExerciseEntities));

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

            List<ExerciseEventEntity> exerciseEventEntities = blockingEntityStore.select(ExerciseEventEntity.class)
                    .join(MasterEventEntity.class).on(MasterEventEntity.ID.eq(ExerciseEventEntity.MASTER_EVENT_ID))
                    .where(MasterEventEntity.CHILD_ID.eq(child.getId()))
                    .get().toList();
            imageFilesToDelete.addAll(getImageFileNamesExerciseEvents(exerciseEventEntities));

            ChildEntity childEntity = blockingEntityStore.findByKey(ChildEntity.class, child.getId());
            imageFilesToDelete.add(childEntity.getImageFileName());
            blockingEntityStore.delete(childEntity);

            return DeleteChildResponse.builder()
                    .request(request)
                    .imageFilesToDelete(imageFilesToDelete)
                    .build();
        }));
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
            DoctorVisit doctorVisit = request.getDoctorVisit();
            Long id = getDoctorVisitId(doctorVisit);
            DoctorVisitEntity doctorVisitEntity = findDoctorVisitEntity(id);

            List<String> imageFilesToDelete = new ArrayList<>();
            if (!TextUtils.isEmpty(doctorVisitEntity.getImageFileName())
                    && !doctorVisitEntity.getImageFileName().equals(doctorVisit.getImageFileName())) {
                imageFilesToDelete.add(doctorVisitEntity.getImageFileName());
            }

            doctorVisitEntity.setFinishDateTime(request.getDateTime());
            doctorVisitEntity.setImageFileName(doctorVisit.getImageFileName());
            doctorVisitEntity.setNote(doctorVisit.getNote());
            blockingEntityStore.update(doctorVisitEntity);

            int count = 0;
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
            MedicineTaking medicineTaking = request.getMedicineTaking();
            Long id = getMedicineTakingId(medicineTaking);
            MedicineTakingEntity medicineTakingEntity = findMedicineTakingEntity(id);

            List<String> imageFilesToDelete = new ArrayList<>();
            if (!TextUtils.isEmpty(medicineTakingEntity.getImageFileName())
                    && !medicineTakingEntity.getImageFileName().equals(medicineTaking.getImageFileName())) {
                imageFilesToDelete.add(medicineTakingEntity.getImageFileName());
            }

            medicineTakingEntity.setFinishDateTime(request.getDateTime());
            medicineTakingEntity.setImageFileName(medicineTaking.getImageFileName());
            medicineTakingEntity.setNote(medicineTaking.getNote());
            blockingEntityStore.update(medicineTakingEntity);

            int count = 0;
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
            } else if (event instanceof ExerciseEvent) {
                ExerciseEvent exerciseEvent = (ExerciseEvent) event;
                imageFilesToDelete.add(exerciseEvent.getImageFileName());
                Long id = getConcreteExerciseId(exerciseEvent.getConcreteExercise());
                ConcreteExerciseEntity concreteExerciseEntity = findConcreteExerciseEntity(id);
                deleteIfPossible(concreteExerciseEntity, imageFilesToDelete);
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
        // Поскольку пользователь не может редактировать ConcreteExercise через интерфейс,
        // можем удалить этот объект сразу, как только не останется привязанных к нему событий
        val events = getExerciseEvents(concreteExerciseEntity.getId());
        if (events.isEmpty()) {
            delete(concreteExerciseEntity, imageFilesToDelete);
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
