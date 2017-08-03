package ru.android.childdiary.domain.interactors.development.achievement;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.domain.interactors.development.achievement.data.ConcreteAchievement;
import ru.android.childdiary.domain.interactors.development.achievement.requests.DeleteConcreteAchievementRequest;
import ru.android.childdiary.domain.interactors.development.achievement.requests.DeleteConcreteAchievementResponse;
import ru.android.childdiary.domain.interactors.dictionaries.GetAchievementsRequest;
import ru.android.childdiary.domain.interactors.development.achievement.requests.UpsertConcreteAchievementRequest;
import ru.android.childdiary.domain.interactors.development.achievement.requests.UpsertConcreteAchievementResponse;
import ru.android.childdiary.domain.interactors.dictionaries.achievements.Achievement;

public interface AchievementRepository {
    Observable<List<ConcreteAchievement>> getConcreteAchievements(@NonNull Child child);

    Observable<UpsertConcreteAchievementResponse> add(@NonNull UpsertConcreteAchievementRequest request);

    Observable<UpsertConcreteAchievementResponse> update(@NonNull UpsertConcreteAchievementRequest request);

    Observable<DeleteConcreteAchievementResponse> delete(@NonNull DeleteConcreteAchievementRequest request);

    Observable<List<Achievement>> getAchievements(@NonNull GetAchievementsRequest request);

    Observable<Achievement> add(@NonNull Achievement achievement);

    Observable<Achievement> delete(@NonNull Achievement achievement);
}
