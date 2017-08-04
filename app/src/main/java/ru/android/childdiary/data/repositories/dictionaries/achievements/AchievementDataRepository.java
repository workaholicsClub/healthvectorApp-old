package ru.android.childdiary.data.repositories.dictionaries.achievements;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.android.childdiary.data.repositories.dictionaries.core.CrudDataRepository;
import ru.android.childdiary.domain.interactors.dictionaries.achievements.data.Achievement;

@Singleton
public class AchievementDataRepository extends CrudDataRepository<Achievement> {
    @Inject
    public AchievementDataRepository(AchievementDbService dbService) {
        super(dbService);
    }
}
