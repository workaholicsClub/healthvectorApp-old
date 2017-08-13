package ru.android.childdiary.domain.dictionaries.achievements.validation;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.data.repositories.dictionaries.achievements.AchievementDataRepository;
import ru.android.childdiary.domain.dictionaries.achievements.data.Achievement;
import ru.android.childdiary.domain.dictionaries.core.validation.DictionaryFieldType;
import ru.android.childdiary.domain.dictionaries.core.validation.DictionaryValidator;

public class AchievementValidator extends DictionaryValidator<Achievement> {
    @Inject
    public AchievementValidator(Context context, AchievementDataRepository repository) {
        super(context, repository);
    }

    @Override
    protected String getName(@NonNull Achievement item) {
        return item.getName();
    }

    @Override
    protected DictionaryFieldType getNameFieldType() {
        return DictionaryFieldType.ACHIEVEMENT_NAME;
    }

    @Override
    protected String getEmptyNameMessage() {
        return context.getString(R.string.enter_achievement_name);
    }
}
