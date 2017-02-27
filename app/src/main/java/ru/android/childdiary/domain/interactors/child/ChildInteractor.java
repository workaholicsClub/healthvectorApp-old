package ru.android.childdiary.domain.interactors.child;

import android.support.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.child.ChildDataRepository;
import ru.android.childdiary.domain.core.Interactor;

public class ChildInteractor implements Interactor, ChildRepository {
    private final Logger logger = LoggerFactory.getLogger(toString());

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
    public Observable<Child> get(@NonNull Long id) {
        return childRepository.get(id);
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
        return childRepository.delete(item)
                .doOnNext(child -> {
                    String path = child.getImageFileName();
                    if (path != null) {
                        boolean result = new File(path).delete();
                        if (result) {
                            logger.debug("file " + path + " deleted");
                        } else {
                            logger.error("file " + path + " not deleted");
                        }
                    }
                });
    }
}
