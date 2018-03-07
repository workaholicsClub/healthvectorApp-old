package ru.android.healthvector.data.repositories.development.achievement;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.achievement.ConcreteAchievementRepository;
import ru.android.healthvector.domain.development.achievement.data.ConcreteAchievement;
import ru.android.healthvector.domain.development.achievement.requests.DeleteConcreteAchievementRequest;
import ru.android.healthvector.domain.development.achievement.requests.DeleteConcreteAchievementResponse;
import ru.android.healthvector.domain.development.achievement.requests.UpsertConcreteAchievementRequest;
import ru.android.healthvector.domain.development.achievement.requests.UpsertConcreteAchievementResponse;
import ru.android.healthvector.utils.ObjectUtils;

@Singleton
public class ConcreteAchievementDataRepository implements ConcreteAchievementRepository {
    private final ConcreteAchievementDbService concreteAchievementDbService;

    @Inject
    public ConcreteAchievementDataRepository(ConcreteAchievementDbService concreteAchievementDbService) {
        this.concreteAchievementDbService = concreteAchievementDbService;
    }

    @Override
    public Observable<List<ConcreteAchievement>> getConcreteAchievements(@NonNull Child child) {
        return concreteAchievementDbService.getConcreteAchievements(child)
                .map(this::sort);
    }

    private List<ConcreteAchievement> sort(@NonNull List<ConcreteAchievement> list) {
        Collections.sort(list, (item1, item2) -> {
            int result;
            if (item1.getDate() == null && item2.getDate() == null) {
                result = ObjectUtils.compare(item1.getFromAge(), item2.getFromAge());
                if (result == 0) {
                    result = ObjectUtils.compare(item1.getToAge(), item2.getToAge());
                }
            } else {
                result = ObjectUtils.compare(item1.getDate(), item2.getDate());
            }
            if (result == 0) {
                return item1.getName().compareTo(item2.getName());
            }
            return result;
        });
        return list;
    }

    @Override
    public Observable<UpsertConcreteAchievementResponse> add(@NonNull UpsertConcreteAchievementRequest request) {
        return concreteAchievementDbService.add(request);
    }

    @Override
    public Observable<UpsertConcreteAchievementResponse> update(@NonNull UpsertConcreteAchievementRequest request) {
        return concreteAchievementDbService.update(request);
    }

    @Override
    public Observable<DeleteConcreteAchievementResponse> delete(@NonNull DeleteConcreteAchievementRequest request) {
        return concreteAchievementDbService.delete(request);
    }
}
