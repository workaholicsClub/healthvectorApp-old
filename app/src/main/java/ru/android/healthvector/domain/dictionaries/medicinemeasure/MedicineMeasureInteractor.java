package ru.android.healthvector.domain.dictionaries.medicinemeasure;

import javax.inject.Inject;

import ru.android.healthvector.data.repositories.dictionaries.medicinemeasure.MedicineMeasureDataRepository;
import ru.android.healthvector.domain.dictionaries.core.BaseDictionaryInteractor;
import ru.android.healthvector.domain.dictionaries.medicinemeasure.data.MedicineMeasure;
import ru.android.healthvector.domain.dictionaries.medicinemeasure.validation.MedicineMeasureValidator;

public class MedicineMeasureInteractor extends BaseDictionaryInteractor<MedicineMeasure> {
    @Inject
    public MedicineMeasureInteractor(MedicineMeasureDataRepository repository,
                                     MedicineMeasureValidator validator) {
        super(repository, validator);
    }

    @Override
    protected MedicineMeasure buildItem(String name) {
        return MedicineMeasure.builder().nameUser(name).build();
    }
}
