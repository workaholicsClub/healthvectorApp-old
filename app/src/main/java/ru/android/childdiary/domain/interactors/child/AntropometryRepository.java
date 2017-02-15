package ru.android.childdiary.domain.interactors.child;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.core.CrudRepository;

public interface AntropometryRepository extends CrudRepository<Antropometry> {
    Observable<List<Antropometry>> getAll(Child child);
}
