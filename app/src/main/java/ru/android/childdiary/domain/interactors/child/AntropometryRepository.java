package ru.android.childdiary.domain.interactors.child;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.core.Repository;

public interface AntropometryRepository extends Repository {
    Observable<List<Antropometry>> getAll(Child child);

    Observable<Antropometry> add(Antropometry antropometry);

    Observable<Antropometry> update(Antropometry antropometry);

    Observable<Antropometry> delete(Antropometry antropometry);
}
