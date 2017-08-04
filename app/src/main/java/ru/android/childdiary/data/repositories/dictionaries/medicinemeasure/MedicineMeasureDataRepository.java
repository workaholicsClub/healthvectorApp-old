package ru.android.childdiary.data.repositories.dictionaries.medicinemeasure;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.android.childdiary.data.repositories.dictionaries.core.CrudDataRepository;
import ru.android.childdiary.domain.interactors.dictionaries.medicinemeasure.data.MedicineMeasure;

@Singleton
public class MedicineMeasureDataRepository extends CrudDataRepository<MedicineMeasure> {
    @Inject
    public MedicineMeasureDataRepository(MedicineMeasureDbService dbService) {
        super(dbService);
    }
}
