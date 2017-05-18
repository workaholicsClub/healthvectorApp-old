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
    private final AntropometryDbService antropometryDbService;

    @Inject
    public AntropometryDataRepository(AntropometryDbService antropometryDbService) {
        this.antropometryDbService = antropometryDbService;
    }

    @Override
    public Observable<List<Antropometry>> getAll(@NonNull Child child) {
        return antropometryDbService.getAll(child);
    }

    @Override
    public Observable<Antropometry> add(@NonNull Antropometry antropometry) {
        return antropometryDbService.add(antropometry);
    }

    @Override
    public Observable<Antropometry> update(@NonNull Antropometry antropometry) {
        return antropometryDbService.update(antropometry);
    }

    @Override
    public Observable<Antropometry> delete(@NonNull Antropometry antropometry) {
        return antropometryDbService.delete(antropometry);
    }
}
