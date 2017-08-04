package ru.android.childdiary.data.repositories.dictionaries.medicines.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.db.entities.dictionaries.MedicineData;
import ru.android.childdiary.data.db.entities.dictionaries.MedicineEntity;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.interactors.dictionaries.medicines.data.Medicine;

public class MedicineMapper implements EntityMapper<MedicineData, MedicineEntity, Medicine> {
    @Inject
    public MedicineMapper() {
    }

    @Override
    public Medicine mapToPlainObject(@NonNull MedicineData medicineData) {
        return Medicine.builder()
                .id(medicineData.getId())
                .name(medicineData.getName())
                .build();
    }

    @Override
    public MedicineEntity mapToEntity(BlockingEntityStore blockingEntityStore,
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

    @Override
    public void fillNonReferencedFields(@NonNull MedicineEntity to, @NonNull Medicine from) {
        to.setName(from.getName());
    }
}
