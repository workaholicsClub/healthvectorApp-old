package ru.android.healthvector.data.repositories.dictionaries.medicinemeasure;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.android.healthvector.data.repositories.dictionaries.core.CrudDataRepository;
import ru.android.healthvector.domain.dictionaries.medicinemeasure.data.MedicineMeasure;

@Singleton
public class MedicineMeasureDataRepository extends CrudDataRepository<MedicineMeasure> {
    @Inject
    public MedicineMeasureDataRepository(MedicineMeasureDbService dbService) {
        super(dbService);
    }
}
