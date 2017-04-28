package ru.android.childdiary.data.repositories.medical.mappers;

import android.support.annotation.NonNull;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.medical.MedicineTakingData;
import ru.android.childdiary.data.entities.medical.MedicineTakingEntity;
import ru.android.childdiary.data.entities.medical.core.MedicineData;
import ru.android.childdiary.data.entities.medical.core.MedicineEntity;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;

public class MedicineTakingMapper {
    public static MedicineTaking mapToPlainObject(@NonNull MedicineTakingData medicineTakingData) {
        MedicineData medicineData = medicineTakingData.getMedicine();
        Medicine medicine = medicineData == null ? null : MedicineMapper.mapToPlainObject(medicineData);
        return MedicineTaking.builder()
                .id(medicineTakingData.getId())
                .medicine(medicine)
                .dateTime(medicineTakingData.getDateTime())
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
        Medicine medicine = medicineTaking.getMedicine();
        if (medicine != null) {
            MedicineEntity medicineEntity = (MedicineEntity) blockingEntityStore.findByKey(MedicineEntity.class, medicine.getId());
            medicineTakingEntity.setMedicine(medicineEntity);
        }
        return medicineTakingEntity;
    }

    private static void fillNonReferencedFields(@NonNull MedicineTakingEntity to, @NonNull MedicineTaking from) {
        to.setDateTime(from.getDateTime());
        to.setNotifyTimeInMinutes(from.getNotifyTimeInMinutes());
        to.setNote(from.getNote());
        to.setImageFileName(from.getImageFileName());
    }
}
