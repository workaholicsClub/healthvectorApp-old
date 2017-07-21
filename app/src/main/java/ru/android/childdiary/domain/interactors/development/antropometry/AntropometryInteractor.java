package ru.android.childdiary.domain.interactors.development.antropometry;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.development.antropometry.AntropometryDataRepository;
import ru.android.childdiary.domain.interactors.child.Child;

public class AntropometryInteractor {
    private final AntropometryRepository antropometryRepository;

    @Inject
    public AntropometryInteractor(AntropometryDataRepository antropometryRepository) {
        this.antropometryRepository = antropometryRepository;
    }

    public Observable<List<Antropometry>> getAll(@NonNull Child child) {
        return antropometryRepository.getAll(child);
    }

    public Observable<Antropometry> add(@NonNull Child child, @NonNull Antropometry item) {
        item = item.toBuilder().child(child).build();
        return antropometryRepository.add(item);
    }

    public Observable<Antropometry> update(@NonNull Antropometry item) {
        return antropometryRepository.update(item);
    }

    public Observable<Antropometry> delete(@NonNull Antropometry item) {
        return antropometryRepository.delete(item);
    }
}
