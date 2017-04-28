package ru.android.childdiary.data.repositories.medical.mappers;

import android.support.annotation.NonNull;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.medical.core.MedicineData;
import ru.android.childdiary.data.entities.medical.core.MedicineEntity;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;

public class MedicineMapper {
    public static Medicine mapToPlainObject(@NonNull MedicineData medicineData) {
        return Medicine.builder()
                .id(medicineData.getId())
                .name(medicineData.getName())
                .build();
    }

    public static MedicineEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                             @NonNull Medicine medicine) {
        MedicineEntity medicineEntity;
        if (medicine.getId() == null) {
            medicineEntity = new MedicineEntity();
        } else {
            medicineEntity = (MedicineEntity) blockingEntityStore.findByKey(MedicineEntity.class, medicine.getId());
        }
        fillNonReferencedFields(medicineEntity, medicine);
        return medicineEntity;
    }

    private static void fillNonReferencedFields(@NonNull MedicineEntity to, @NonNull Medicine from) {
        to.setName(from.getName());
    }
}
