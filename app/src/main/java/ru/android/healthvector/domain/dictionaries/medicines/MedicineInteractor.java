package ru.android.healthvector.domain.dictionaries.medicines;

import javax.inject.Inject;

import ru.android.healthvector.data.repositories.dictionaries.medicines.MedicineDataRepository;
import ru.android.healthvector.domain.dictionaries.core.BaseDictionaryInteractor;
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;
import ru.android.healthvector.domain.dictionaries.medicines.validation.MedicineValidator;

public class MedicineInteractor extends BaseDictionaryInteractor<Medicine> {
    @Inject
    public MedicineInteractor(MedicineDataRepository repository,
                              MedicineValidator validator) {
        super(repository, validator);
    }

    @Override
    protected Medicine buildItem(String name) {
        return Medicine.builder().nameUser(name).build();
    }
}
