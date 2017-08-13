package ru.android.childdiary.data.repositories.dictionaries.achievements;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.db.entities.dictionaries.AchievementEntity;
import ru.android.childdiary.data.repositories.core.exceptions.RestrictDeleteException;
import ru.android.childdiary.data.repositories.dictionaries.achievements.mappers.AchievementMapper;
import ru.android.childdiary.data.repositories.dictionaries.core.BaseCrudDbService;
import ru.android.childdiary.domain.dictionaries.achievements.data.Achievement;
import ru.android.childdiary.utils.ObjectUtils;

@Singleton
public class AchievementDbService extends BaseCrudDbService<Achievement> {
    private final AchievementMapper achievementMapper;

    @Inject
    public AchievementDbService(ReactiveEntityStore<Persistable> dataStore,
                                AchievementMapper achievementMapper) {
        super(dataStore);
        this.achievementMapper = achievementMapper;
    }

    @Override
    public Observable<List<Achievement>> getAll() {
        return dataStore.select(AchievementEntity.class)
                .orderBy(AchievementEntity.PREDEFINED.desc(), AchievementEntity.ORDER_NUMBER, AchievementEntity.NAME, AchievementEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, achievementMapper));
    }

    @Override
    public Observable<Achievement> add(@NonNull Achievement achievement) {
        return DbUtils.insertObservable(blockingEntityStore, achievement, achievementMapper);
    }

    @Override
    public Observable<Achievement> update(@NonNull Achievement achievement) {
        return DbUtils.updateObservable(blockingEntityStore, achievement, achievementMapper);
    }

    @Override
    public Observable<Achievement> delete(@NonNull Achievement achievement) {
        if (ObjectUtils.isTrue(achievement.getIsPredefined())) {
            return Observable.error(new RestrictDeleteException("Can't remove predefined achievement"));
        }
        return DbUtils.deleteObservable(blockingEntityStore, AchievementEntity.class, achievement, achievement.getId());
    }
}
