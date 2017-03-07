package ru.android.childdiary.data.repositories.child;

import android.support.annotation.NonNull;

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
    public Observable<List<Antropometry>> getAll(@NonNull Child child) {
        return dbService.getAll(child);
    }

    @Override
    public Observable<Antropometry> add(@NonNull Child child, @NonNull Antropometry antropometry) {
        return dbService.add(child, antropometry);
    }

    @Override
    public Observable<Antropometry> update(@NonNull Antropometry antropometry) {
        return dbService.update(antropometry);
    }

    @Override
    public Observable<Antropometry> delete(@NonNull Antropometry antropometry) {
        return dbService.delete(antropometry);
    }
}
