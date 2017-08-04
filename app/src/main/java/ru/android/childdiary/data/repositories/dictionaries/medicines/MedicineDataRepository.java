package ru.android.childdiary.data.repositories.dictionaries.medicines;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.android.childdiary.data.repositories.dictionaries.core.CrudDataRepository;
import ru.android.childdiary.domain.interactors.dictionaries.medicines.data.Medicine;

@Singleton
public class MedicineDataRepository extends CrudDataRepository<Medicine> {
    @Inject
    public MedicineDataRepository(MedicineDbService dbService) {
        super(dbService);
    }
}
