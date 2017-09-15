package ru.android.childdiary.data.repositories.dictionaries.achievements;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.Getter;
import lombok.Setter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.repositories.dictionaries.core.CrudDataRepository;
import ru.android.childdiary.data.types.AchievementType;
import ru.android.childdiary.domain.core.LocalizationUtils;
import ru.android.childdiary.domain.development.achievement.data.ConcreteAchievement;
import ru.android.childdiary.domain.dictionaries.achievements.data.Achievement;

@Singleton
public class AchievementDataRepository extends CrudDataRepository<Achievement> {
    private static final String LANGUAGE_EN = "en", LANGUAGE_RU = "ru";

    private final Map<AchievementType, List<AchievementGroup>> achievementGroupsMap = new HashMap<AchievementType, List<AchievementGroup>>() {{
        put(AchievementType.HEARING_AND_VISION, Arrays.asList(
                new AchievementGroup(1.0, null, R.array.hearing_and_vision__1__),
                new AchievementGroup(2.0, null, R.array.hearing_and_vision__2__),
                new AchievementGroup(3.0, null, R.array.hearing_and_vision__3__),
                new AchievementGroup(4.0, null, R.array.hearing_and_vision__4__),
                new AchievementGroup(5.0, null, R.array.hearing_and_vision__5__),
                new AchievementGroup(6.0, null, R.array.hearing_and_vision__6__),
                new AchievementGroup(7.0, null, R.array.hearing_and_vision__7__),
                new AchievementGroup(8.0, null, R.array.hearing_and_vision__8__),
                new AchievementGroup(9.0, null, R.array.hearing_and_vision__9__),
                new AchievementGroup(10.0, null, R.array.hearing_and_vision__10__),
                new AchievementGroup(11.0, null, R.array.hearing_and_vision__11__),
                new AchievementGroup(12.0, null, R.array.hearing_and_vision__12__)));
        put(AchievementType.GROSS_MOTOR_SKILLS, Arrays.asList(
                new AchievementGroup(1.0, null, R.array.gross_motor_skills__1__),
                new AchievementGroup(2.0, null, R.array.gross_motor_skills__2__),
                new AchievementGroup(3.0, null, R.array.gross_motor_skills__3__),
                new AchievementGroup(4.0, null, R.array.gross_motor_skills__4__),
                new AchievementGroup(5.0, null, R.array.gross_motor_skills__5__),
                new AchievementGroup(6.0, null, R.array.gross_motor_skills__6__),
                new AchievementGroup(7.0, null, R.array.gross_motor_skills__7__),
                new AchievementGroup(8.0, null, R.array.gross_motor_skills__8__),
                new AchievementGroup(9.0, null, R.array.gross_motor_skills__9__),
                new AchievementGroup(10.0, null, R.array.gross_motor_skills__10__),
                new AchievementGroup(11.0, null, R.array.gross_motor_skills__11__),
                new AchievementGroup(12.0, null, R.array.gross_motor_skills__12__),
                new AchievementGroup(12.0, 18.0, R.array.gross_motor_skills__12__18),
                new AchievementGroup(18.0, 24.0, R.array.gross_motor_skills__18__24)));
        put(AchievementType.FINE_MOTOR_SKILLS, Arrays.asList(
                new AchievementGroup(1.0, null, R.array.fine_motor_skills__1__),
                new AchievementGroup(2.0, null, R.array.fine_motor_skills__2__),
                new AchievementGroup(3.0, null, R.array.fine_motor_skills__3__),
                new AchievementGroup(4.0, null, R.array.fine_motor_skills__4__),
                new AchievementGroup(5.0, null, R.array.fine_motor_skills__5__),
                new AchievementGroup(6.0, null, R.array.fine_motor_skills__6__),
                new AchievementGroup(7.0, null, R.array.fine_motor_skills__7__),
                new AchievementGroup(8.0, null, R.array.fine_motor_skills__8__),
                new AchievementGroup(9.0, null, R.array.fine_motor_skills__9__),
                new AchievementGroup(10.0, null, R.array.fine_motor_skills__10__),
                new AchievementGroup(11.0, null, R.array.fine_motor_skills__11__),
                new AchievementGroup(12.0, null, R.array.fine_motor_skills__12__),
                new AchievementGroup(12.0, 18.0, R.array.fine_motor_skills__12__18),
                new AchievementGroup(18.0, 24.0, R.array.fine_motor_skills__18__24),
                new AchievementGroup(24.0, 30.0, R.array.fine_motor_skills__24__30),
                new AchievementGroup(30.0, 36.0, R.array.fine_motor_skills__30__36)));
        put(AchievementType.SOCIAL_DEVELOPMENT, Arrays.asList(
                new AchievementGroup(1.0, null, R.array.social_development__1__),
                new AchievementGroup(2.0, null, R.array.social_development__2__),
                new AchievementGroup(3.0, null, R.array.social_development__3__),
                new AchievementGroup(4.0, null, R.array.social_development__4__),
                new AchievementGroup(5.0, null, R.array.social_development__5__),
                new AchievementGroup(6.0, null, R.array.social_development__6__),
                new AchievementGroup(7.0, null, R.array.social_development__7__),
                new AchievementGroup(8.0, null, R.array.social_development__8__),
                new AchievementGroup(9.0, null, R.array.social_development__9__),
                new AchievementGroup(10.0, null, R.array.social_development__10__),
                new AchievementGroup(11.0, null, R.array.social_development__11__),
                new AchievementGroup(12.0, null, R.array.social_development__12__),
                new AchievementGroup(12.0, 18.0, R.array.social_development__12__18),
                new AchievementGroup(18.0, 24.0, R.array.social_development__18__24),
                new AchievementGroup(24.0, 30.0, R.array.social_development__24__30),
                new AchievementGroup(30.0, 36.0, R.array.social_development__30__36)));
        put(AchievementType.SPEECH_DEVELOPMENT, Arrays.asList(
                new AchievementGroup(1.0, null, R.array.speech_development__1__),
                new AchievementGroup(1.5, null, R.array.speech_development__1_5__),
                new AchievementGroup(2.0, null, R.array.speech_development__2__),
                new AchievementGroup(3.0, null, R.array.speech_development__3__),
                new AchievementGroup(4.0, null, R.array.speech_development__4__),
                new AchievementGroup(5.0, null, R.array.speech_development__5__),
                new AchievementGroup(6.0, null, R.array.speech_development__6__),
                new AchievementGroup(7.0, null, R.array.speech_development__7__),
                new AchievementGroup(8.0, null, R.array.speech_development__8__),
                new AchievementGroup(9.0, null, R.array.speech_development__9__),
                new AchievementGroup(10.0, null, R.array.speech_development__10__),
                new AchievementGroup(11.0, null, R.array.speech_development__11__),
                new AchievementGroup(12.0, null, R.array.speech_development__12__),
                new AchievementGroup(12.0, 18.0, R.array.speech_development__12__18),
                new AchievementGroup(18.0, 24.0, R.array.speech_development__18__24),
                new AchievementGroup(24.0, 30.0, R.array.speech_development__24__30),
                new AchievementGroup(30.0, 36.0, R.array.speech_development__30__36)));
        put(AchievementType.SELF_DEPENDENCE_SKILLS, Arrays.asList(
                new AchievementGroup(3.0, null, R.array.self_dependence_skills__3__),
                new AchievementGroup(4.0, 5.0, R.array.self_dependence_skills__4__5),
                new AchievementGroup(6.0, null, R.array.self_dependence_skills__6__),
                new AchievementGroup(7.0, null, R.array.self_dependence_skills__7__),
                new AchievementGroup(8.0, null, R.array.self_dependence_skills__8__),
                new AchievementGroup(9.0, null, R.array.self_dependence_skills__9__),
                new AchievementGroup(10.0, null, R.array.self_dependence_skills__10__),
                new AchievementGroup(11.0, null, R.array.self_dependence_skills__11__),
                new AchievementGroup(12.0, null, R.array.self_dependence_skills__12__),
                new AchievementGroup(12.0, 18.0, R.array.self_dependence_skills__12__18),
                new AchievementGroup(18.0, 24.0, R.array.self_dependence_skills__18__24),
                new AchievementGroup(24.0, 30.0, R.array.self_dependence_skills__24__30),
                new AchievementGroup(30.0, 36.0, R.array.self_dependence_skills__30__36)));
    }};

    private final Context context;

    @Inject
    public AchievementDataRepository(Context context, AchievementDbService dbService) {
        super(dbService);
        this.context = context;
    }

    public List<ConcreteAchievement> generatePredefinedConcreteAchievements() {
        LocalizationUtils.fillFromResources(context, LANGUAGE_EN, resources -> {
            for (AchievementType achievementType : AchievementType.values()) {
                List<AchievementGroup> achievementGroups = achievementGroupsMap.get(achievementType);
                for (AchievementGroup achievementGroup : achievementGroups) {
                    String[] strings = resources.getStringArray(achievementGroup.getArrayId());
                    achievementGroup.setStringsEn(strings);
                }
            }
        });
        LocalizationUtils.fillFromResources(context, LANGUAGE_RU, resources -> {
            for (AchievementType achievementType : AchievementType.values()) {
                List<AchievementGroup> achievementGroups = achievementGroupsMap.get(achievementType);
                for (AchievementGroup achievementGroup : achievementGroups) {
                    String[] strings = resources.getStringArray(achievementGroup.getArrayId());
                    achievementGroup.setStringsRu(strings);
                }
            }
        });
        List<ConcreteAchievement> concreteAchievements = new ArrayList<>();
        for (AchievementType achievementType : AchievementType.values()) {
            List<AchievementGroup> achievementGroups = achievementGroupsMap.get(achievementType);
            for (AchievementGroup achievementGroup : achievementGroups) {
                for (int i = 0; i < achievementGroup.getCount(); ++i) {
                    String[] strings = achievementGroup.getPair(i);
                    ConcreteAchievement concreteAchievement = createPredefinedConcreteAchievement(
                            achievementType, strings[0], strings[1],
                            achievementGroup.getFromAge(), achievementGroup.getToAge());
                    concreteAchievements.add(concreteAchievement);
                }
            }
        }
        return concreteAchievements;
    }

    private ConcreteAchievement createPredefinedConcreteAchievement(@NonNull AchievementType achievementType,
                                                                    @NonNull String nameEn,
                                                                    @NonNull String nameRu,
                                                                    @Nullable Double fromAge,
                                                                    @Nullable Double toAge) {
        return ConcreteAchievement.builder()
                .id(null)
                .child(null)
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

    @Getter
    private static class AchievementGroup {
        @NonNull
        private final Double fromAge;
        @Nullable
        private final Double toAge;
        @ArrayRes
        private final int arrayId;
        @Setter
        String[] stringsEn, stringsRu;

        public AchievementGroup(@NonNull Double fromAge,
                                @Nullable Double toAge,
                                @ArrayRes int arrayId) {
            this.fromAge = fromAge;
            this.toAge = toAge;
            this.arrayId = arrayId;
        }

        public int getCount() {
            return Math.min(stringsEn.length, stringsRu.length);
        }

        public String[] getPair(int index) {
            return new String[]{stringsEn[index], stringsRu[index]};
        }
    }
}
