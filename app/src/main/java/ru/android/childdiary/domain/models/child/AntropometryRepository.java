package ru.android.childdiary.domain.models.child;

import java.util.List;

import io.reactivex.Single;
import ru.android.childdiary.domain.core.CrudRepository;

public interface AntropometryRepository extends CrudRepository<Antropometry> {
    Single<List<Antropometry>> getAll(Child child);
}
