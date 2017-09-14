package ru.android.childdiary.data.repositories.child;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.db.entities.child.ChildEntity;
import ru.android.childdiary.data.db.entities.development.ConcreteAchievementEntity;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.development.achievement.mappers.ConcreteAchievementMapper;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.development.achievement.data.ConcreteAchievement;

@Singleton
public class ChildDbService {
    private final ReactiveEntityStore<Persistable> dataStore;
    private final BlockingEntityStore<Persistable> blockingEntityStore;
    private final ChildMapper childMapper;
    private final ConcreteAchievementMapper concreteAchievementMapper;

    @Inject
    public ChildDbService(ReactiveEntityStore<Persistable> dataStore,
                          ChildMapper childMapper,
                          ConcreteAchievementMapper concreteAchievementMapper) {
        this.dataStore = dataStore;
        this.blockingEntityStore = dataStore.toBlocking();
        this.childMapper = childMapper;
        this.concreteAchievementMapper = concreteAchievementMapper;
    }

    public Observable<List<Child>> getAll() {
        return dataStore.select(ChildEntity.class)
                .orderBy(ChildEntity.NAME, ChildEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, childMapper));
    }

    public Observable<Child> add(@NonNull Child child, @NonNull List<ConcreteAchievement> concreteAchievements) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            Child result = DbUtils.insert(blockingEntityStore, child, childMapper);
            List<ConcreteAchievementEntity> concreteAchievementEntities = new ArrayList<>();
            for (ConcreteAchievement concreteAchievement : concreteAchievements) {
                ConcreteAchievementEntity concreteAchievementEntity = concreteAchievementMapper.mapToEntity(blockingEntityStore, concreteAchievement);
                concreteAchievementEntities.add(concreteAchievementEntity);
            }
            blockingEntityStore.insert(concreteAchievementEntities);
            return result;
        }));
    }

    public Observable<Child> update(@NonNull Child child) {
        return DbUtils.updateObservable(blockingEntityStore, child, childMapper);
    }
}
