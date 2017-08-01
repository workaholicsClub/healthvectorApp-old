package ru.android.childdiary.data.repositories.development.achievement;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.repositories.development.achievement.mappers.AchievementMapper;
import ru.android.childdiary.data.repositories.development.achievement.mappers.ConcreteAchievementMapper;

@Singleton
public class AchievementDbService {
    private final ReactiveEntityStore<Persistable> dataStore;
    private final BlockingEntityStore<Persistable> blockingEntityStore;
    private final AchievementMapper achievementMapper;
    private final ConcreteAchievementMapper concreteAchievementMapper;

    @Inject
    public AchievementDbService(ReactiveEntityStore<Persistable> dataStore,
                                AchievementMapper achievementMapper,
                                ConcreteAchievementMapper concreteAchievementMapper) {
        this.dataStore = dataStore;
        this.blockingEntityStore = dataStore.toBlocking();
        this.achievementMapper = achievementMapper;
        this.concreteAchievementMapper = concreteAchievementMapper;
    }
}
