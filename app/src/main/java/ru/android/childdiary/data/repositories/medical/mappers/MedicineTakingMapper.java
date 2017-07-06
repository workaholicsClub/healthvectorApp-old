package ru.android.childdiary.data.repositories.medical.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.db.entities.child.ChildData;
import ru.android.childdiary.data.db.entities.child.ChildEntity;
import ru.android.childdiary.data.db.entities.core.RepeatParametersData;
import ru.android.childdiary.data.db.entities.core.RepeatParametersEntity;
import ru.android.childdiary.data.db.entities.medical.MedicineTakingData;
import ru.android.childdiary.data.db.entities.medical.MedicineTakingEntity;
import ru.android.childdiary.data.db.entities.medical.core.MedicineData;
import ru.android.childdiary.data.db.entities.medical.core.MedicineEntity;
import ru.android.childdiary.data.db.entities.medical.core.MedicineMeasureData;
import ru.android.childdiary.data.db.entities.medical.core.MedicineMeasureEntity;
import ru.android.childdiary.data.repositories.calendar.mappers.RepeatParametersMapper;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;

public class MedicineTakingMapper implements EntityMapper<MedicineTakingData, MedicineTakingEntity, MedicineTaking> {
    private final ChildMapper childMapper;
    private final MedicineMapper medicineMapper;
    private final MedicineMeasureMapper medicineMeasureMapper;
    private final RepeatParametersMapper repeatParametersMapper;

    @Inject
    public MedicineTakingMapper(ChildMapper childMapper,
                                MedicineMapper medicineMapper,
                                MedicineMeasureMapper medicineMeasureMapper,
                                RepeatParametersMapper repeatParametersMapper) {
        this.childMapper = childMapper;
        this.medicineMapper = medicineMapper;
        this.medicineMeasureMapper = medicineMeasureMapper;
        this.repeatParametersMapper = repeatParametersMapper;
    }

    @Override
    public MedicineTaking mapToPlainObject(@NonNull MedicineTakingData medicineTakingData) {
        ChildData childData = medicineTakingData.getChild();
        Child child = childData == null ? null : childMapper.mapToPlainObject(childData);
        MedicineData medicineData = medicineTakingData.getMedicine();
        Medicine medicine = medicineData == null ? null : medicineMapper.mapToPlainObject(medicineData);
        MedicineMeasureData medicineMeasureData = medicineTakingData.getMedicineMeasure();
        MedicineMeasure medicineMeasure = medicineMeasureData == null ? null : medicineMeasureMapper.mapToPlainObject(medicineMeasureData);
        RepeatParametersData repeatParametersData = medicineTakingData.getRepeatParameters();
        RepeatParameters repeatParameters = repeatParametersData == null ? null : repeatParametersMapper.mapToPlainObject(repeatParametersData);
        return MedicineTaking.builder()
                .id(medicineTakingData.getId())
                .child(child)
                .medicine(medicine)
                .amount(medicineTakingData.getAmount())
                .medicineMeasure(medicineMeasure)
                .repeatParameters(repeatParameters)
                .dateTime(medicineTakingData.getDateTime())
                .finishDateTime(medicineTakingData.getFinishDateTime())
                .isExported(medicineTakingData.isExported())
                .notifyTimeInMinutes(medicineTakingData.getNotifyTimeInMinutes())
                .note(medicineTakingData.getNote())
                .imageFileName(medicineTakingData.getImageFileName())
                .isDeleted(medicineTakingData.isDeleted())
                .build();
    }

    @Override
    public MedicineTakingEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                            @NonNull MedicineTaking medicineTaking) {
        MedicineTakingEntity medicineTakingEntity;
        if (medicineTaking.getId() == null) {
            medicineTakingEntity = new MedicineTakingEntity();
        } else {
            medicineTakingEntity = (MedicineTakingEntity) blockingEntityStore.findByKey(MedicineTakingEntity.class, medicineTaking.getId());
        }
        fillNonReferencedFields(medicineTakingEntity, medicineTaking);
        Child child = medicineTaking.getChild();
        if (child != null) {
            ChildEntity childEntity = (ChildEntity) blockingEntityStore.findByKey(ChildEntity.class, child.getId());
            medicineTakingEntity.setChild(childEntity);
        }
        Medicine medicine = medicineTaking.getMedicine();
        if (medicine != null) {
            MedicineEntity medicineEntity = (MedicineEntity) blockingEntityStore.findByKey(MedicineEntity.class, medicine.getId());
            medicineTakingEntity.setMedicine(medicineEntity);
        }
        MedicineMeasure medicineMeasure = medicineTaking.getMedicineMeasure();
        if (medicineMeasure != null) {
            MedicineMeasureEntity medicineMeasureEntity = (MedicineMeasureEntity) blockingEntityStore.findByKey(MedicineMeasureEntity.class, medicineMeasure.getId());
            medicineTakingEntity.setMedicineMeasure(medicineMeasureEntity);
        }
        RepeatParameters repeatParameters = medicineTaking.getRepeatParameters();
        if (repeatParameters != null) {
            RepeatParametersEntity repeatParametersEntity = (RepeatParametersEntity) blockingEntityStore.findByKey(RepeatParametersEntity.class, repeatParameters.getId());
            medicineTakingEntity.setRepeatParameters(repeatParametersEntity);
        }
        return medicineTakingEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull MedicineTakingEntity to, @NonNull MedicineTaking from) {
        to.setAmount(from.getAmount());
        to.setDateTime(from.getDateTime());
        to.setFinishDateTime(from.getFinishDateTime());
        to.setExported(from.getIsExported());
        to.setNotifyTimeInMinutes(from.getNotifyTimeInMinutes());
        to.setNote(from.getNote());
        to.setImageFileName(from.getImageFileName());
        to.setDeleted(from.getIsDeleted());
    }
}
