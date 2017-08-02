package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.db.entities.calendar.events.MedicineTakingEventData;
import ru.android.childdiary.data.db.entities.calendar.events.MedicineTakingEventEntity;
import ru.android.childdiary.data.db.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.db.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.db.entities.child.ChildData;
import ru.android.childdiary.data.db.entities.medical.MedicineTakingData;
import ru.android.childdiary.data.db.entities.medical.MedicineTakingEntity;
import ru.android.childdiary.data.db.entities.medical.core.MedicineData;
import ru.android.childdiary.data.db.entities.medical.core.MedicineEntity;
import ru.android.childdiary.data.db.entities.medical.core.MedicineMeasureData;
import ru.android.childdiary.data.db.entities.medical.core.MedicineMeasureEntity;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.data.repositories.medical.mappers.MedicineMapper;
import ru.android.childdiary.data.repositories.medical.mappers.MedicineMeasureMapper;
import ru.android.childdiary.data.repositories.medical.mappers.MedicineTakingMapper;
import ru.android.childdiary.domain.interactors.calendar.events.MedicineTakingEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;

public class MedicineTakingEventMapper implements EntityMapper<MedicineTakingEventData, MedicineTakingEventEntity, MedicineTakingEvent> {
    private final ChildMapper childMapper;
    private final MedicineTakingMapper medicineTakingMapper;
    private final MedicineMapper medicineMapper;
    private final MedicineMeasureMapper medicineMeasureMapper;

    @Inject
    public MedicineTakingEventMapper(ChildMapper childMapper,
                                     MedicineTakingMapper medicineTakingMapper,
                                     MedicineMapper medicineMapper,
                                     MedicineMeasureMapper medicineMeasureMapper) {
        this.childMapper = childMapper;
        this.medicineTakingMapper = medicineTakingMapper;
        this.medicineMapper = medicineMapper;
        this.medicineMeasureMapper = medicineMeasureMapper;
    }

    @Override
    public MedicineTakingEvent mapToPlainObject(@NonNull MedicineTakingEventData medicineTakingEventData) {
        MasterEventData masterEventData = medicineTakingEventData.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? Child.NULL : childMapper.mapToPlainObject(childData);
        MedicineTakingData medicineTakingData = medicineTakingEventData.getMedicineTaking();
        MedicineTaking medicineTaking = medicineTakingData == null ? null : medicineTakingMapper.mapToPlainObject(medicineTakingData);
        MedicineData medicineData = medicineTakingEventData.getMedicine();
        Medicine medicine = medicineData == null ? null : medicineMapper.mapToPlainObject(medicineData);
        MedicineMeasureData medicineMeasureData = medicineTakingEventData.getMedicineMeasure();
        MedicineMeasure medicineMeasure = medicineMeasureData == null ? null : medicineMeasureMapper.mapToPlainObject(medicineMeasureData);
        return MedicineTakingEvent.builder()
                .id(medicineTakingEventData.getId())
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .dateTime(masterEventData.getDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .child(child)
                .linearGroup(masterEventData.getLinearGroup())
                .medicineTaking(medicineTaking)
                .medicine(medicine)
                .amount(medicineTakingEventData.getAmount())
                .medicineMeasure(medicineMeasure)
                .imageFileName(medicineTakingEventData.getImageFileName())
                .build();
    }

    @Override
    public MedicineTakingEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                                 @NonNull MedicineTakingEvent medicineTakingEvent) {
        MedicineTakingEventEntity medicineTakingEventEntity;
        if (medicineTakingEvent.getId() == null) {
            medicineTakingEventEntity = new MedicineTakingEventEntity();
        } else {
            medicineTakingEventEntity = (MedicineTakingEventEntity) blockingEntityStore.findByKey(MedicineTakingEventEntity.class, medicineTakingEvent.getId());
        }
        fillNonReferencedFields(medicineTakingEventEntity, medicineTakingEvent);

        MasterEventEntity masterEventEntity = (MasterEventEntity) blockingEntityStore.findByKey(MasterEventEntity.class, medicineTakingEvent.getMasterEventId());
        medicineTakingEventEntity.setMasterEvent(masterEventEntity);

        MedicineTaking medicineTaking = medicineTakingEvent.getMedicineTaking();
        if (medicineTaking != null) {
            MedicineTakingEntity medicineTakingEntity = (MedicineTakingEntity) blockingEntityStore.findByKey(MedicineTakingEntity.class, medicineTaking.getId());
            medicineTakingEventEntity.setMedicineTaking(medicineTakingEntity);
        }
        Medicine medicine = medicineTakingEvent.getMedicine();
        if (medicine != null) {
            MedicineEntity medicineEntity = (MedicineEntity) blockingEntityStore.findByKey(MedicineEntity.class, medicine.getId());
            medicineTakingEventEntity.setMedicine(medicineEntity);
        }
        MedicineMeasure medicineMeasure = medicineTakingEvent.getMedicineMeasure();
        if (medicineMeasure != null) {
            MedicineMeasureEntity medicineMeasureEntity = (MedicineMeasureEntity) blockingEntityStore.findByKey(MedicineMeasureEntity.class, medicineMeasure.getId());
            medicineTakingEventEntity.setMedicineMeasure(medicineMeasureEntity);
        }
        return medicineTakingEventEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull MedicineTakingEventEntity to, @NonNull MedicineTakingEvent from) {
        to.setAmount(from.getAmount());
        to.setImageFileName(from.getImageFileName());
    }
}
