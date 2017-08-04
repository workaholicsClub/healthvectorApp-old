package ru.android.childdiary.domain.interactors.dictionaries.achievements;

import javax.inject.Inject;

import ru.android.childdiary.data.repositories.dictionaries.achievements.AchievementDataRepository;
import ru.android.childdiary.domain.interactors.dictionaries.achievements.data.Achievement;
import ru.android.childdiary.domain.interactors.dictionaries.achievements.validation.AchievementValidator;
import ru.android.childdiary.domain.interactors.dictionaries.core.BaseDictionaryInteractor;

public class AchievementInteractor extends BaseDictionaryInteractor<Achievement> {
    @Inject
    public AchievementInteractor(AchievementDataRepository repository,
                                 AchievementValidator validator) {
        super(repository, validator);
    }

    @Override
    protected Achievement buildItem(String name) {
        return Achievement.builder().name(name).build();
    }
}
