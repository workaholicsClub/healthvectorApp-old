package ru.android.childdiary.utils.strings;

import android.content.Context;
import android.support.annotation.Nullable;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.AchievementType;

public class AchievementUtils {
    @Nullable
    public static String toString(Context context, @Nullable AchievementType achievementType) {
        if (achievementType == null) {
            return null;
        }
        switch (achievementType) {
            case HEARING_AND_VISION:
                return context.getString(R.string.hearing_and_vision);
            case GROSS_MOTOR_SKILLS:
                return context.getString(R.string.gross_motor_skills);
            case FINE_MOTOR_SKILLS:
                return context.getString(R.string.fine_motor_skills);
            case SOCIAL_DEVELOPMENT:
                return context.getString(R.string.social_development);
            case SPEECH_DEVELOPMENT:
                return context.getString(R.string.speech_development);
            case SELF_DEPENDENCE_SKILLS:
                return context.getString(R.string.self_dependence_skills);
        }
        return null;
    }
}
