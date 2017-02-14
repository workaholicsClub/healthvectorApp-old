package ru.android.childdiary.data.repositories.child;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Single;
import ru.android.childdiary.domain.models.child.Antropometry;
import ru.android.childdiary.domain.models.child.AntropometryRepository;
import ru.android.childdiary.domain.models.child.Child;

@Singleton
public class AntropometryDataRepository implements AntropometryRepository {
    private final AntropometryDbService dbService;

    public AntropometryDataRepository(AntropometryDbService dbService) {
        this.dbService = dbService;
    }

    @Override
    public Single<List<Antropometry>> getAll() {
        return dbService.getAll();
    }

    @Override
    public Single<List<Antropometry>> getAll(Child child) {
        return dbService.getAll(child);
    }

    @Override
    public Single<Antropometry> add(Antropometry antropometry) {
        return dbService.add(antropometry);
    }

    @Override
    public Single<Antropometry> update(Antropometry antropometry) {
        return dbService.update(antropometry);
    }

    @Override
    public Single<Antropometry> delete(Antropometry antropometry) {
        return dbService.delete(antropometry);
    }
}
