package ru.android.childdiary.data.repositories.development.achievement;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.db.entities.development.ConcreteAchievementEntity;
import ru.android.childdiary.data.repositories.development.achievement.mappers.ConcreteAchievementMapper;
import ru.android.childdiary.data.repositories.dictionaries.achievements.mappers.AchievementMapper;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.development.achievement.data.ConcreteAchievement;
import ru.android.childdiary.domain.development.achievement.requests.DeleteConcreteAchievementRequest;
import ru.android.childdiary.domain.development.achievement.requests.DeleteConcreteAchievementResponse;
import ru.android.childdiary.domain.development.achievement.requests.UpsertConcreteAchievementRequest;
import ru.android.childdiary.domain.development.achievement.requests.UpsertConcreteAchievementResponse;

@Singleton
public class ConcreteAchievementDbService {
    private final ReactiveEntityStore<Persistable> dataStore;
    private final BlockingEntityStore<Persistable> blockingEntityStore;
    private final ConcreteAchievementMapper concreteAchievementMapper;

    @Inject
    public ConcreteAchievementDbService(ReactiveEntityStore<Persistable> dataStore,
                                        AchievementMapper achievementMapper,
                                        ConcreteAchievementMapper concreteAchievementMapper) {
        this.dataStore = dataStore;
        this.blockingEntityStore = dataStore.toBlocking();
        this.concreteAchievementMapper = concreteAchievementMapper;
    }

    public Observable<List<ConcreteAchievement>> getConcreteAchievements(@NonNull Child child) {
        return dataStore.select(ConcreteAchievementEntity.class)
                .where(ConcreteAchievementEntity.CHILD_ID.eq(child.getId()))
                .orderBy(ConcreteAchievementEntity.CONCRETE_ACHIEVEMENT_DATE.desc(),
                        ConcreteAchievementEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, concreteAchievementMapper))
                .map(this::sort);
    }

    private List<ConcreteAchievement> sort(@NonNull List<ConcreteAchievement> concreteAchievements) {
        Collections.sort(concreteAchievements,
                (o1, o2) -> o1.getDate() == null && o2.getDate() == null ? 0 : (o1.getDate() != null ? 1 : -1));
        return concreteAchievements;
    }

    public Observable<UpsertConcreteAchievementResponse> add(@NonNull UpsertConcreteAchievementRequest request) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            ConcreteAchievement concreteAchievement = request.getConcreteAchievement();

            List<String> imageFilesToDelete = Collections.emptyList();

            ConcreteAchievement result = DbUtils.insert(blockingEntityStore, concreteAchievement, concreteAchievementMapper);

            return UpsertConcreteAchievementResponse.builder()
                    .request(request)
                    .concreteAchievement(result)
                    .imageFilesToDelete(imageFilesToDelete)
                    .build();
        }));
    }

    public Observable<UpsertConcreteAchievementResponse> update(@NonNull UpsertConcreteAchievementRequest request) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            ConcreteAchievement concreteAchievement = request.getConcreteAchievement();
            ConcreteAchievementEntity oldConcreteAchievementEntity = blockingEntityStore.findByKey(ConcreteAchievementEntity.class, concreteAchievement.getId());

            List<String> imageFilesToDelete = new ArrayList<>();
            if (!TextUtils.isEmpty(oldConcreteAchievementEntity.getImageFileName())
                    && !oldConcreteAchievementEntity.getImageFileName().equals(concreteAchievement.getImageFileName())) {
                imageFilesToDelete.add(oldConcreteAchievementEntity.getImageFileName());
            }

            ConcreteAchievement result = DbUtils.update(blockingEntityStore, concreteAchievement, concreteAchievementMapper);

            return UpsertConcreteAchievementResponse.builder()
                    .request(request)
                    .concreteAchievement(result)
                    .imageFilesToDelete(imageFilesToDelete)
                    .build();
        }));
    }

    public Observable<DeleteConcreteAchievementResponse> delete(@NonNull DeleteConcreteAchievementRequest request) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            ConcreteAchievement concreteAchievement = request.getConcreteAchievement();
            ConcreteAchievementEntity concreteAchievementEntity = blockingEntityStore.findByKey(ConcreteAchievementEntity.class, concreteAchievement.getId());

            List<String> imageFilesToDelete = new ArrayList<>();
            imageFilesToDelete.add(concreteAchievementEntity.getImageFileName());

            blockingEntityStore.delete(concreteAchievementEntity);

            return DeleteConcreteAchievementResponse.builder()
                    .request(request)
                    .imageFilesToDelete(imageFilesToDelete)
                    .build();
        }));
    }
}
