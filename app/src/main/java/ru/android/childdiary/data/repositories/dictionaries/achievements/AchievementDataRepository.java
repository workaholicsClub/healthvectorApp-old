package ru.android.childdiary.data.repositories.dictionaries.achievements;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.android.childdiary.data.repositories.dictionaries.core.CrudDataRepository;
import ru.android.childdiary.data.types.AchievementType;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.development.achievement.data.ConcreteAchievement;
import ru.android.childdiary.domain.dictionaries.achievements.data.Achievement;

@Singleton
public class AchievementDataRepository extends CrudDataRepository<Achievement> {
    private static final String LANGUAGE_EN = "en", LANGUAGE_RU = "ru";

    private final Context context;

    @Inject
    public AchievementDataRepository(Context context, AchievementDbService dbService) {
        super(dbService);
        this.context = context;
    }

    public List<ConcreteAchievement> generatePredefinedConcreteAchievements(@NonNull Child child) {
        return Collections.emptyList();
    }

    private ConcreteAchievement createPredefinedConcreteAchievement(@NonNull Child child,
                                                                    @NonNull AchievementType achievementType,
                                                                    @NonNull String nameEn,
                                                                    @NonNull String nameRu,
                                                                    @Nullable Double fromAge,
                                                                    @Nullable Double toAge) {
        return ConcreteAchievement.builder()
                .id(null)
                .child(child)
                .achievementType(achievementType)
                .nameEn(nameEn)
                .nameRu(nameRu)
                .date(null)
                .note(null)
                .imageFileName(null)
                .isPredefined(true)
                .fromAge(fromAge)
                .toAge(toAge)
                .build();
    }
}
