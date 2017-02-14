package ru.android.childdiary.data.repositories.child;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Single;
import ru.android.childdiary.domain.models.child.Child;
import ru.android.childdiary.domain.models.child.ChildRepository;

@Singleton
public class ChildDataRepository implements ChildRepository {
    private final ChildDbService dbService;

    public ChildDataRepository(ChildDbService dbService) {
        this.dbService = dbService;
    }

    @Override
    public Single<List<Child>> getAll() {
        return dbService.getAll();
    }

    @Override
    public Single<Child> add(Child child) {
        return dbService.add(child);
    }

    @Override
    public Single<Child> update(Child child) {
        return dbService.update(child);
    }

    @Override
    public Single<Child> delete(Child child) {
        return dbService.delete(child);
    }
}
