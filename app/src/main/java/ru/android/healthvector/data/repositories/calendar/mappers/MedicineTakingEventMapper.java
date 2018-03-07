package ru.android.healthvector.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.healthvector.data.db.entities.calendar.MedicineTakingEventData;
import ru.android.healthvector.data.db.entities.calendar.MedicineTakingEventEntity;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventData;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventEntity;
import ru.android.healthvector.data.db.entities.child.ChildData;
import ru.android.healthvector.data.db.entities.dictionaries.MedicineData;
import ru.android.healthvector.data.db.entities.dictionaries.MedicineEntity;
import ru.android.healthvector.data.db.entities.dictionaries.MedicineMeasureData;
import ru.android.healthvector.data.db.entities.dictionaries.MedicineMeasureEntity;
import ru.android.healthvector.data.db.entities.medical.MedicineTakingData;
import ru.android.healthvector.data.db.entities.medical.MedicineTakingEntity;
import ru.android.healthvector.data.repositories.child.mappers.ChildMapper;
import ru.android.healthvector.data.repositories.core.mappers.EntityMapper;
import ru.android.healthvector.data.repositories.dictionaries.medicinemeasure.mappers.MedicineMeasureMapper;
import ru.android.healthvector.data.repositories.dictionaries.medicines.mappers.MedicineMapper;
import ru.android.healthvector.data.repositories.medical.mappers.MedicineTakingMapper;
import ru.android.healthvector.domain.calendar.data.MedicineTakingEvent;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.dictionaries.medicinemeasure.data.MedicineMeasure;
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;
import ru.android.healthvector.domain.medical.data.MedicineTaking;

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
                .notifyDateTime(masterEventData.getNotifyDateTime())
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
