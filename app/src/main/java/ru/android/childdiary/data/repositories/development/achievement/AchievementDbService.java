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
import ru.android.childdiary.data.db.entities.development.achievement.AchievementEntity;
import ru.android.childdiary.data.db.entities.development.achievement.ConcreteAchievementEntity;
import ru.android.childdiary.data.repositories.development.achievement.mappers.AchievementMapper;
import ru.android.childdiary.data.repositories.development.achievement.mappers.ConcreteAchievementMapper;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.achievement.Achievement;
import ru.android.childdiary.domain.interactors.development.achievement.ConcreteAchievement;
import ru.android.childdiary.domain.interactors.development.achievement.requests.DeleteConcreteAchievementRequest;
import ru.android.childdiary.domain.interactors.development.achievement.requests.DeleteConcreteAchievementResponse;
import ru.android.childdiary.domain.interactors.development.achievement.requests.GetAchievementsRequest;
import ru.android.childdiary.domain.interactors.development.achievement.requests.UpsertConcreteAchievementRequest;
import ru.android.childdiary.domain.interactors.development.achievement.requests.UpsertConcreteAchievementResponse;
import ru.android.childdiary.utils.ObjectUtils;

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

    public Observable<List<ConcreteAchievement>> getConcreteAchievements(@NonNull Child child) {
        return dataStore.select(ConcreteAchievementEntity.class)
                .where(ConcreteAchievementEntity.CHILD_ID.eq(child.getId()))
                .orderBy(ConcreteAchievementEntity.PREDEFINED, ConcreteAchievementEntity.ORDER_NUMBER,
                        ConcreteAchievementEntity.CONCRETE_ACHIEVEMENT_DATE,
                        ConcreteAchievementEntity.NAME, ConcreteAchievementEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, concreteAchievementMapper));
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

    public Observable<List<Achievement>> getAchievements(@NonNull GetAchievementsRequest request) {
        return dataStore.select(AchievementEntity.class)
                .orderBy(AchievementEntity.PREDEFINED, AchievementEntity.ORDER_NUMBER, AchievementEntity.NAME, AchievementEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, achievementMapper))
                .map(achievements -> filter(request, achievements));
    }

    private List<Achievement> filter(@NonNull GetAchievementsRequest request, @NonNull List<Achievement> achievements) {
        return Observable.fromIterable(achievements)
                .filter(achievement -> !request.isPredefined() || ObjectUtils.isTrue(achievement.getIsPredefined()))
                .toList()
                .blockingGet();
    }

    public Observable<Achievement> add(@NonNull Achievement achievement) {
        return DbUtils.insertObservable(blockingEntityStore, achievement, achievementMapper);
    }

    public Observable<Achievement> delete(@NonNull Achievement achievement) {
        return DbUtils.deleteObservable(blockingEntityStore, AchievementEntity.class, achievement, achievement.getId());
    }
}
