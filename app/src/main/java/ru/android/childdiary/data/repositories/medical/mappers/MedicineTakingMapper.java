package ru.android.childdiary.data.repositories.medical.mappers;

import android.support.annotation.NonNull;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.core.RepeatParametersData;
import ru.android.childdiary.data.entities.calendar.events.core.RepeatParametersEntity;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.data.entities.medical.MedicineTakingData;
import ru.android.childdiary.data.entities.medical.MedicineTakingEntity;
import ru.android.childdiary.data.entities.medical.core.MedicineData;
import ru.android.childdiary.data.entities.medical.core.MedicineEntity;
import ru.android.childdiary.data.entities.medical.core.MedicineMeasureData;
import ru.android.childdiary.data.entities.medical.core.MedicineMeasureEntity;
import ru.android.childdiary.data.repositories.calendar.mappers.RepeatParametersMapper;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.domain.interactors.calendar.events.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;

public class MedicineTakingMapper {
    public static MedicineTaking mapToPlainObject(@NonNull MedicineTakingData medicineTakingData) {
        ChildData childData = medicineTakingData.getChild();
        Child child = childData == null ? null : ChildMapper.mapToPlainObject(childData);
        MedicineData medicineData = medicineTakingData.getMedicine();
        Medicine medicine = medicineData == null ? null : MedicineMapper.mapToPlainObject(medicineData);
        MedicineMeasureData medicineMeasureData = medicineTakingData.getMedicineMeasure();
        MedicineMeasure medicineMeasure = medicineMeasureData == null ? null : MedicineMeasureMapper.mapToPlainObject(medicineMeasureData);
        RepeatParametersData repeatParametersData = medicineTakingData.getRepeatParameters();
        RepeatParameters repeatParameters = repeatParametersData == null ? null : RepeatParametersMapper.mapToPlainObject(repeatParametersData);
        return MedicineTaking.builder()
                .id(medicineTakingData.getId())
                .child(child)
                .medicine(medicine)
                .amount(medicineTakingData.getAmount())
                .medicineMeasure(medicineMeasure)
                .repeatParameters(repeatParameters)
                .dateTime(medicineTakingData.getDateTime())
                .finishDateTime(medicineTakingData.getFinishDateTime())
                .exported(medicineTakingData.getExported())
                .notifyTimeInMinutes(medicineTakingData.getNotifyTimeInMinutes())
                .note(medicineTakingData.getNote())
                .imageFileName(medicineTakingData.getImageFileName())
                .build();
    }

    public static MedicineTakingEntity mapToEntity(BlockingEntityStore blockingEntityStore,
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

    private static void fillNonReferencedFields(@NonNull MedicineTakingEntity to, @NonNull MedicineTaking from) {
        to.setAmount(from.getAmount());
        to.setDateTime(from.getDateTime());
        to.setFinishDateTime(from.getFinishDateTime());
        to.setExported(from.getExported());
        to.setNotifyTimeInMinutes(from.getNotifyTimeInMinutes());
        to.setNote(from.getNote());
        to.setImageFileName(from.getImageFileName());
    }
}
