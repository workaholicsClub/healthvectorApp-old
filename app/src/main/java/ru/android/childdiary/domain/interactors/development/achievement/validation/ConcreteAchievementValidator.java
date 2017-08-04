package ru.android.childdiary.domain.interactors.development.achievement.validation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.core.validation.core.ValidationException;
import ru.android.childdiary.domain.interactors.core.validation.core.Validator;
import ru.android.childdiary.domain.interactors.development.achievement.data.ConcreteAchievement;

public class ConcreteAchievementValidator extends Validator<ConcreteAchievement, AchievementValidationResult> {
    private final Context context;

    @Inject
    public ConcreteAchievementValidator(Context context) {
        this.context = context;
    }

    @Override
    public List<AchievementValidationResult> validate(@NonNull ConcreteAchievement concreteAchievement) {
        List<AchievementValidationResult> results = new ArrayList<>();

        AchievementValidationResult result = new AchievementValidationResult(AchievementFieldType.CONCRETE_ACHIEVEMENT_NAME);
        if (TextUtils.isEmpty(concreteAchievement.getName())) {
            result.addMessage(context.getString(R.string.enter_concrete_achievement_name));
        }
        results.add(result);

        return results;
    }

    @Override
    protected ValidationException createException(@NonNull List<AchievementValidationResult> results) {
        return new AchievementValidationException(results);
    }
}
