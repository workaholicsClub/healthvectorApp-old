package ru.android.healthvector.data.repositories.dictionaries.medicines.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.healthvector.data.db.entities.dictionaries.MedicineData;
import ru.android.healthvector.data.db.entities.dictionaries.MedicineEntity;
import ru.android.healthvector.data.repositories.core.mappers.EntityMapper;
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;

public class MedicineMapper implements EntityMapper<MedicineData, MedicineEntity, Medicine> {
    @Inject
    public MedicineMapper() {
    }

    @Override
    public Medicine mapToPlainObject(@NonNull MedicineData medicineData) {
        return Medicine.builder()
                .id(medicineData.getId())
                .nameEn(medicineData.getNameEn())
                .nameRu(medicineData.getNameRu())
                .nameUser(medicineData.getNameUser())
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
        to.setNameEn(from.getNameEn());
        to.setNameRu(from.getNameRu());
        to.setNameUser(from.getNameUser());
    }
}
