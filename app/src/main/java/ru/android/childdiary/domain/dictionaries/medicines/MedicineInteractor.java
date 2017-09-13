package ru.android.childdiary.domain.dictionaries.medicines;

import javax.inject.Inject;

import ru.android.childdiary.data.repositories.dictionaries.medicines.MedicineDataRepository;
import ru.android.childdiary.domain.dictionaries.core.BaseDictionaryInteractor;
import ru.android.childdiary.domain.dictionaries.medicines.data.Medicine;
import ru.android.childdiary.domain.dictionaries.medicines.validation.MedicineValidator;

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
