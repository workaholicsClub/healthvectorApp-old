package ru.android.childdiary.domain.interactors.child;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.child.AntropometryDataRepository;
import ru.android.childdiary.domain.core.Interactor;

public class AntropometryInteractor implements Interactor, AntropometryRepository {
    private final AntropometryDataRepository antropometryRepository;

    @Inject
    public AntropometryInteractor(AntropometryDataRepository antropometryRepository) {
        this.antropometryRepository = antropometryRepository;
    }

    @Override
    public Observable<List<Antropometry>> getAll(@NonNull Child child) {
        return antropometryRepository.getAll(child);
    }

    @Override
    public Observable<Antropometry> add(@NonNull Child child, @NonNull Antropometry item) {
        return antropometryRepository.add(child, item);
    }

    @Override
    public Observable<Antropometry> update(@NonNull Antropometry item) {
        return antropometryRepository.update(item);
    }

    @Override
    public Observable<Antropometry> delete(@NonNull Antropometry item) {
        return antropometryRepository.update(item);
    }
}
