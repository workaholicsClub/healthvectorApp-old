package ru.android.healthvector.domain.dictionaries.achievements;

import javax.inject.Inject;

import ru.android.healthvector.data.repositories.dictionaries.achievements.AchievementDataRepository;
import ru.android.healthvector.domain.dictionaries.achievements.data.Achievement;
import ru.android.healthvector.domain.dictionaries.achievements.validation.AchievementValidator;
import ru.android.healthvector.domain.dictionaries.core.BaseDictionaryInteractor;

public class AchievementInteractor extends BaseDictionaryInteractor<Achievement> {
    @Inject
    public AchievementInteractor(AchievementDataRepository repository,
                                 AchievementValidator validator) {
        super(repository, validator);
    }

    @Override
    protected Achievement buildItem(String name) {
        return Achievement.builder().nameUser(name).build();
    }
}
