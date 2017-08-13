package ru.android.childdiary.domain.dictionaries.medicinemeasure;

import javax.inject.Inject;

import ru.android.childdiary.data.repositories.dictionaries.medicinemeasure.MedicineMeasureDataRepository;
import ru.android.childdiary.domain.dictionaries.core.BaseDictionaryInteractor;
import ru.android.childdiary.domain.dictionaries.medicinemeasure.data.MedicineMeasure;
import ru.android.childdiary.domain.dictionaries.medicinemeasure.validation.MedicineMeasureValidator;

public class MedicineMeasureInteractor extends BaseDictionaryInteractor<MedicineMeasure> {
    @Inject
    public MedicineMeasureInteractor(MedicineMeasureDataRepository repository,
                                     MedicineMeasureValidator validator) {
        super(repository, validator);
    }

    @Override
    protected MedicineMeasure buildItem(String name) {
        return MedicineMeasure.builder().name(name).build();
    }
}
