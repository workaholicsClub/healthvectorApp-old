package ru.android.childdiary.domain.interactors.child;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.requery.reactivex.ReactiveResult;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.data.repositories.child.ChildDataRepository;
import ru.android.childdiary.domain.core.Interactor;

public class ChildInteractor implements Interactor, ChildRepository {
    private final ChildDataRepository childRepository;

    @Inject
    public ChildInteractor(ChildDataRepository childRepository) {
        this.childRepository = childRepository;
    }

    @Override
    public Observable<List<Child>> getAll() {
        return childRepository.getAll();
    }

    @Override
    public Observable<Child> add(Child item) {
        return childRepository.add(item);
    }

    @Override
    public Observable<Child> update(Child item) {
        return childRepository.update(item);
    }

    @Override
    public Observable<Child> delete(Child item) {
        return childRepository.delete(item);
    }
}
