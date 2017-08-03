package ru.android.childdiary.domain.interactors.development.achievement.validation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.R;
import ru.android.childdiary.data.repositories.development.achievement.AchievementDataRepository;
import ru.android.childdiary.domain.interactors.core.validation.ValidationException;
import ru.android.childdiary.domain.interactors.core.validation.Validator;
import ru.android.childdiary.domain.interactors.dictionaries.achievements.Achievement;
import ru.android.childdiary.domain.interactors.development.achievement.AchievementRepository;
import ru.android.childdiary.domain.interactors.dictionaries.GetAchievementsRequest;

public class AchievementValidator extends Validator<Achievement, AchievementValidationResult> {
    private final Context context;
    private final AchievementRepository achievementRepository;

    @Inject
    public AchievementValidator(Context context, AchievementDataRepository achievementRepository) {
        this.context = context;
        this.achievementRepository = achievementRepository;
    }

    @Override
    public List<AchievementValidationResult> validate(@NonNull Achievement achievement) {
        List<AchievementValidationResult> results = new ArrayList<>();

        AchievementValidationResult result;

        if (TextUtils.isEmpty(achievement.getName())) {
            result = new AchievementValidationResult(AchievementFieldType.ACHIEVEMENT_NAME);
            result.addMessage(context.getString(R.string.enter_achievement_name));
            results.add(result);
        } else {
            result = new AchievementValidationResult(AchievementFieldType.ACHIEVEMENT_NAME);
            results.add(result);

            long count = Observable.fromIterable(
                    achievementRepository.getAchievements(GetAchievementsRequest.builder()
                            .build())
                            .blockingFirst())
                    .filter(d -> !TextUtils.isEmpty(d.getName()))
                    .map(Achievement::getName)
                    .filter(name -> name.equals(achievement.getName()))
                    .count()
                    .blockingGet();

            if (count > 0) {
                result = new AchievementValidationResult(null);
                result.addMessage(context.getString(R.string.the_value_already_exists));
                results.add(result);
            }
        }

        return results;
    }

    @Override
    protected ValidationException createException(@NonNull List<AchievementValidationResult> results) {
        return new AchievementValidationException(results);
    }
}
