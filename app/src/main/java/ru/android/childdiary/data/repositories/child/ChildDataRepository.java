package ru.android.childdiary.data.repositories.child;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildRepository;

@Singleton
public class ChildDataRepository implements ChildRepository {
    private final ChildDbService dbService;

    @Inject
    public ChildDataRepository(ChildDbService dbService) {
        this.dbService = dbService;
    }

    @Override
    public Observable<List<Child>> getAll() {
        return dbService.getAll();
    }

    @Override
    public Observable<Child> add(@NonNull Child child) {
        return dbService.add(child);
    }

    @Override
    public Observable<Child> update(@NonNull Child child) {
        return dbService.update(child);
    }

    @Override
    public Observable<Child> delete(@NonNull Child child) {
        return dbService.delete(child);
    }
}
