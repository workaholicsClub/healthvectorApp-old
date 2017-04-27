package ru.android.childdiary.domain.interactors.child;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.child.AntropometryDataRepository;
import ru.android.childdiary.domain.core.Interactor;

public class AntropometryInteractor implements Interactor {
    private final AntropometryRepository antropometryRepository;

    @Inject
    public AntropometryInteractor(AntropometryDataRepository antropometryRepository) {
        this.antropometryRepository = antropometryRepository;
    }

    public Observable<List<Antropometry>> getAll(@NonNull Child child) {
        return antropometryRepository.getAll(child);
    }

    public Observable<Antropometry> add(@NonNull Child child, @NonNull Antropometry item) {
        return antropometryRepository.add(child, item);
    }

    public Observable<Antropometry> update(@NonNull Antropometry item) {
        return antropometryRepository.update(item);
    }

    public Observable<Antropometry> delete(@NonNull Antropometry item) {
        return antropometryRepository.update(item);
    }
}
