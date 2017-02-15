package ru.android.childdiary.domain.interactors.child;

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
    public Observable<List<Antropometry>> getAll(Child child) {
        return antropometryRepository.getAll(child);
    }

    @Override
    public Observable<List<Antropometry>> getAll() {
        return antropometryRepository.getAll();
    }

    @Override
    public Observable<Antropometry> add(Antropometry item) {
        return antropometryRepository.add(item);
    }

    @Override
    public Observable<Antropometry> update(Antropometry item) {
        return antropometryRepository.update(item);
    }

    @Override
    public Observable<Antropometry> delete(Antropometry item) {
        return antropometryRepository.update(item);
    }
}
