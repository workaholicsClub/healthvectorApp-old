package ru.android.childdiary.data.repositories.child;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.db.entities.child.ChildEntity;
import ru.android.childdiary.data.db.entities.development.achievement.AchievementEntity;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.development.achievement.mappers.AchievementMapper;
import ru.android.childdiary.data.repositories.development.achievement.mappers.ConcreteAchievementMapper;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.achievement.Achievement;
import ru.android.childdiary.domain.interactors.development.achievement.ConcreteAchievement;

@Singleton
public class ChildDbService {
    private final ReactiveEntityStore<Persistable> dataStore;
    private final BlockingEntityStore<Persistable> blockingEntityStore;
    private final ChildMapper childMapper;
    private final ConcreteAchievementMapper concreteAchievementMapper;
    private final AchievementMapper achievementMapper;

    @Inject
    public ChildDbService(ReactiveEntityStore<Persistable> dataStore,
                          ChildMapper childMapper,
                          ConcreteAchievementMapper concreteAchievementMapper,
                          AchievementMapper achievementMapper) {
        this.dataStore = dataStore;
        this.blockingEntityStore = dataStore.toBlocking();
        this.childMapper = childMapper;
        this.concreteAchievementMapper = concreteAchievementMapper;
        this.achievementMapper = achievementMapper;
    }

    public Observable<List<Child>> getAll() {
        return dataStore.select(ChildEntity.class)
                .orderBy(ChildEntity.NAME, ChildEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, childMapper));
    }

    public Observable<Child> add(@NonNull Child child) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            Child result = DbUtils.insert(blockingEntityStore, child, childMapper);
            List<AchievementEntity> achievementEntities = blockingEntityStore.select(AchievementEntity.class)
                    .where(AchievementEntity.PREDEFINED.eq(true))
                    .get()
                    .toList();
            for (AchievementEntity achievementEntity : achievementEntities) {
                Achievement achievement = achievementMapper.mapToPlainObject(achievementEntity);
                ConcreteAchievement concreteAchievement = ConcreteAchievement.builder()
                        .id(null)
                        .child(result)
                        .achievement(achievement)
                        .name(achievement.getName())
                        .date(null)
                        .note(null)
                        .imageFileName(null)
                        .isPredefined(true)
                        .orderNumber(achievement.getOrderNumber())
                        .build();
                DbUtils.insert(blockingEntityStore, concreteAchievement, concreteAchievementMapper);
            }
            return result;
        }));
    }

    public Observable<Child> update(@NonNull Child child) {
        return DbUtils.updateObservable(blockingEntityStore, child, childMapper);
    }
}
