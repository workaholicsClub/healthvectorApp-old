package ru.android.childdiary.data.repositories.child;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.android.childdiary.domain.interactors.child.Antropometry;
import ru.android.childdiary.domain.interactors.child.AntropometryRepository;
import ru.android.childdiary.domain.interactors.child.Child;

@Singleton
public class AntropometryDataRepository implements AntropometryRepository {
    private final AntropometryDbService dbService;

    @Inject
    public AntropometryDataRepository(AntropometryDbService dbService) {
        this.dbService = dbService;
    }

    @Override
    public Observable<List<Antropometry>> getAll(Child child) {
        return dbService.getAll(child);
    }

    @Override
    public Observable<Antropometry> add(Antropometry antropometry) {
        return dbService.add(antropometry);
    }

    @Override
    public Observable<Antropometry> update(Antropometry antropometry) {
        return dbService.update(antropometry);
    }

    @Override
    public Observable<Antropometry> delete(Antropometry antropometry) {
        return dbService.delete(antropometry);
    }
}
