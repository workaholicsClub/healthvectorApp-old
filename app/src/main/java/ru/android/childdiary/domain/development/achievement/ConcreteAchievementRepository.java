package ru.android.childdiary.domain.development.achievement;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.development.achievement.data.ConcreteAchievement;
import ru.android.childdiary.domain.development.achievement.requests.DeleteConcreteAchievementRequest;
import ru.android.childdiary.domain.development.achievement.requests.DeleteConcreteAchievementResponse;
import ru.android.childdiary.domain.development.achievement.requests.UpsertConcreteAchievementRequest;
import ru.android.childdiary.domain.development.achievement.requests.UpsertConcreteAchievementResponse;

public interface ConcreteAchievementRepository {
    Observable<List<ConcreteAchievement>> getConcreteAchievements(@NonNull Child child);

    Observable<UpsertConcreteAchievementResponse> add(@NonNull UpsertConcreteAchievementRequest request);

    Observable<UpsertConcreteAchievementResponse> update(@NonNull UpsertConcreteAchievementRequest request);

    Observable<DeleteConcreteAchievementResponse> delete(@NonNull DeleteConcreteAchievementRequest request);
}
