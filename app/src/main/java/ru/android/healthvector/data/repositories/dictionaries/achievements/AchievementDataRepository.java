package ru.android.healthvector.data.repositories.dictionaries.achievements;

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
import ru.android.healthvector.R;
import ru.android.healthvector.data.repositories.dictionaries.core.CrudDataRepository;
import ru.android.healthvector.data.types.AchievementType;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.core.LocalizationUtils;
import ru.android.healthvector.domain.development.achievement.data.ConcreteAchievement;
import ru.android.healthvector.domain.dictionaries.achievements.data.Achievement;
import ru.android.healthvector.utils.strings.TimeUtils;

@Singleton
public class AchievementDataRepository extends CrudDataRepository<Achievement> {
    private static final String LANGUAGE_EN = "en", LANGUAGE_RU = "ru";

    private final Map<AchievementType, List<AchievementGroup>> achievementGroupsMap = new HashMap<AchievementType, List<AchievementGroup>>() {{
        put(AchievementType.HEARING_AND_VISION, Arrays.asList(
                new AchievementGroup(1.0, null, R.array.hearing_and_vision__1__, 0.0, 3.0),
                new AchievementGroup(2.0, null, R.array.hearing_and_vision__2__, 2.0, 4.0),
                new AchievementGroup(3.0, null, R.array.hearing_and_vision__3__, 3.0, 5.0),
                new AchievementGroup(4.0, null, R.array.hearing_and_vision__4__, 4.0, 6.0),
                new AchievementGroup(5.0, null, R.array.hearing_and_vision__5__, 5.0, 7.0),
                new AchievementGroup(6.0, null, R.array.hearing_and_vision__6__, 6.0, 8.0),
                new AchievementGroup(7.0, null, R.array.hearing_and_vision__7__, 7.0, 9.0),
                new AchievementGroup(8.0, null, R.array.hearing_and_vision__8__, 8.0, 10.0),
                new AchievementGroup(9.0, null, R.array.hearing_and_vision__9__, 9.0, 11.0),
                new AchievementGroup(10.0, null, R.array.hearing_and_vision__10__, 10.0, 12.0),
                new AchievementGroup(11.0, null, R.array.hearing_and_vision__11__, 11.0, 13.0),
                new AchievementGroup(12.0, null, R.array.hearing_and_vision__12__, 12.0, null)));
        put(AchievementType.GROSS_MOTOR_SKILLS, Arrays.asList(
                new AchievementGroup(1.0, null, R.array.gross_motor_skills__1__, 0.0, 3.0),
                new AchievementGroup(2.0, null, R.array.gross_motor_skills__2__, 2.0, 4.0),
                new AchievementGroup(3.0, null, R.array.gross_motor_skills__3__, 3.0, 5.0),
                new AchievementGroup(4.0, null, R.array.gross_motor_skills__4__, 4.0, 6.0),
                new AchievementGroup(5.0, null, R.array.gross_motor_skills__5__, 5.0, 7.0),
                new AchievementGroup(6.0, null, R.array.gross_motor_skills__6__, 6.0, 8.0),
                new AchievementGroup(7.0, null, R.array.gross_motor_skills__7__, 7.0, 9.0),
                new AchievementGroup(8.0, null, R.array.gross_motor_skills__8__, 8.0, 10.0),
                new AchievementGroup(9.0, null, R.array.gross_motor_skills__9__, 9.0, 11.0),
                new AchievementGroup(10.0, null, R.array.gross_motor_skills__10__, 10.0, 12.0),
                new AchievementGroup(11.0, null, R.array.gross_motor_skills__11__, 11.0, 13.0),
                new AchievementGroup(12.0, null, R.array.gross_motor_skills__12__, 12.0, 18.0),
                new AchievementGroup(12.0, 18.0, R.array.gross_motor_skills__12__18, 12.0, 24.0),
                new AchievementGroup(18.0, 24.0, R.array.gross_motor_skills__18__24, 18.0, 30.0),
                new AchievementGroup(24.0, 30.0, R.array.gross_motor_skills__24__30, 24.0, 36.0),
                new AchievementGroup(30.0, 36.0, R.array.gross_motor_skills__30__36, 30.0, null)));
        put(AchievementType.FINE_MOTOR_SKILLS, Arrays.asList(
                new AchievementGroup(1.0, null, R.array.fine_motor_skills__1__, 0.0, 3.0),
                new AchievementGroup(2.0, null, R.array.fine_motor_skills__2__, 2.0, 4.0),
                new AchievementGroup(3.0, null, R.array.fine_motor_skills__3__, 3.0, 5.0),
                new AchievementGroup(4.0, null, R.array.fine_motor_skills__4__, 4.0, 6.0),
                new AchievementGroup(5.0, null, R.array.fine_motor_skills__5__, 5.0, 7.0),
                new AchievementGroup(6.0, null, R.array.fine_motor_skills__6__, 6.0, 8.0),
                new AchievementGroup(7.0, null, R.array.fine_motor_skills__7__, 7.0, 9.0),
                new AchievementGroup(8.0, null, R.array.fine_motor_skills__8__, 8.0, 10.0),
                new AchievementGroup(9.0, null, R.array.fine_motor_skills__9__, 9.0, 11.0),
                new AchievementGroup(10.0, null, R.array.fine_motor_skills__10__, 10.0, 12.0),
                new AchievementGroup(11.0, null, R.array.fine_motor_skills__11__, 11.0, 13.0),
                new AchievementGroup(12.0, null, R.array.fine_motor_skills__12__, 12.0, 18.0),
                new AchievementGroup(12.0, 18.0, R.array.fine_motor_skills__12__18, 12.0, 24.0),
                new AchievementGroup(18.0, 24.0, R.array.fine_motor_skills__18__24, 18.0, 30.0),
                new AchievementGroup(24.0, 30.0, R.array.fine_motor_skills__24__30, 24.0, 36.0),
                new AchievementGroup(30.0, 36.0, R.array.fine_motor_skills__30__36, 30.0, null)));
        put(AchievementType.SOCIAL_DEVELOPMENT, Arrays.asList(
                new AchievementGroup(1.0, null, R.array.social_development__1__, 0.0, 3.0),
                new AchievementGroup(2.0, null, R.array.social_development__2__, 2.0, 4.0),
                new AchievementGroup(3.0, null, R.array.social_development__3__, 3.0, 5.0),
                new AchievementGroup(4.0, null, R.array.social_development__4__, 4.0, 6.0),
                new AchievementGroup(5.0, null, R.array.social_development__5__, 5.0, 7.0),
                new AchievementGroup(6.0, null, R.array.social_development__6__, 6.0, 8.0),
                new AchievementGroup(7.0, null, R.array.social_development__7__, 7.0, 9.0),
                new AchievementGroup(8.0, null, R.array.social_development__8__, 8.0, 10.0),
                new AchievementGroup(9.0, null, R.array.social_development__9__, 9.0, 11.0),
                new AchievementGroup(10.0, null, R.array.social_development__10__, 10.0, 12.0),
                new AchievementGroup(11.0, null, R.array.social_development__11__, 11.0, 13.0),
                new AchievementGroup(12.0, null, R.array.social_development__12__, 12.0, 18.0),
                new AchievementGroup(12.0, 18.0, R.array.social_development__12__18, 12.0, 24.0),
                new AchievementGroup(18.0, 24.0, R.array.social_development__18__24, 18.0, 30.0),
                new AchievementGroup(24.0, 30.0, R.array.social_development__24__30, 24.0, 36.0),
                new AchievementGroup(30.0, 36.0, R.array.social_development__30__36, 30.0, null)));
        put(AchievementType.SPEECH_DEVELOPMENT, Arrays.asList(
                new AchievementGroup(1.0, null, R.array.speech_development__1__, 0.0, 2.0),
                new AchievementGroup(1.5, null, R.array.speech_development__1_5__, 1.5, 3.0),
                new AchievementGroup(2.0, null, R.array.speech_development__2__, 2.0, 4.0),
                new AchievementGroup(3.0, null, R.array.speech_development__3__, 3.0, 5.0),
                new AchievementGroup(4.0, null, R.array.speech_development__4__, 4.0, 6.0),
                new AchievementGroup(5.0, null, R.array.speech_development__5__, 5.0, 7.0),
                new AchievementGroup(6.0, null, R.array.speech_development__6__, 6.0, 8.0),
                new AchievementGroup(7.0, null, R.array.speech_development__7__, 7.0, 9.0),
                new AchievementGroup(8.0, null, R.array.speech_development__8__, 8.0, 10.0),
                new AchievementGroup(9.0, null, R.array.speech_development__9__, 9.0, 11.0),
                new AchievementGroup(10.0, null, R.array.speech_development__10__, 10.0, 12.0),
                new AchievementGroup(11.0, null, R.array.speech_development__11__, 11.0, 13.0),
                new AchievementGroup(12.0, null, R.array.speech_development__12__, 12.0, 18.0),
                new AchievementGroup(12.0, 18.0, R.array.speech_development__12__18, 12.0, 24.0),
                new AchievementGroup(18.0, 24.0, R.array.speech_development__18__24, 18.0, 30.0),
                new AchievementGroup(24.0, 30.0, R.array.speech_development__24__30, 24.0, 36.0),
                new AchievementGroup(30.0, 36.0, R.array.speech_development__30__36, 30.0, null)));
        put(AchievementType.SELF_DEPENDENCE_SKILLS, Arrays.asList(
                new AchievementGroup(3.0, null, R.array.self_dependence_skills__3__, 0.0, 6.0),
                new AchievementGroup(4.0, 5.0, R.array.self_dependence_skills__4__5, 4.0, 7.0),
                new AchievementGroup(6.0, null, R.array.self_dependence_skills__6__, 6.0, 8.0),
                new AchievementGroup(7.0, null, R.array.self_dependence_skills__7__, 7.0, 9.0),
                new AchievementGroup(8.0, null, R.array.self_dependence_skills__8__, 8.0, 10.0),
                new AchievementGroup(9.0, null, R.array.self_dependence_skills__9__, 9.0, 11.0),
                new AchievementGroup(10.0, null, R.array.self_dependence_skills__10__, 10.0, 12.0),
                new AchievementGroup(11.0, null, R.array.self_dependence_skills__11__, 11.0, 13.0),
                new AchievementGroup(12.0, null, R.array.self_dependence_skills__12__, 12.0, 18.0),
                new AchievementGroup(12.0, 18.0, R.array.self_dependence_skills__12__18, 12.0, 24.0),
                new AchievementGroup(18.0, 24.0, R.array.self_dependence_skills__18__24, 18.0, 30.0),
                new AchievementGroup(24.0, 30.0, R.array.self_dependence_skills__24__30, 24.0, 36.0),
                new AchievementGroup(30.0, 36.0, R.array.self_dependence_skills__30__36, 30.0, null)));
    }};

    private final Context context;

    @Inject
    public AchievementDataRepository(Context context, AchievementDbService dbService) {
        super(dbService);
        this.context = context;
    }

    public List<ConcreteAchievement> generatePredefinedConcreteAchievements(@NonNull Child child) {
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
        double months = TimeUtils.getMonths(child);
        List<ConcreteAchievement> concreteAchievements = new ArrayList<>();
        for (AchievementType achievementType : AchievementType.values()) {
            List<AchievementGroup> achievementGroups = achievementGroupsMap.get(achievementType);
            boolean included = false;
            for (AchievementGroup achievementGroup : achievementGroups) {
                included = included
                        || achievementGroup.getToMonths() != null
                        && achievementGroup.getFromMonths() <= months && months < achievementGroup.getToMonths()
                        || achievementGroup.getToMonths() == null
                        && achievementGroup.getFromMonths() <= months;
                if (!included) {
                    continue;
                }
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
        private final double fromAge;
        @Nullable
        private final Double toAge;
        @ArrayRes
        private final int arrayId;
        private final double fromMonths;
        @Nullable
        private final Double toMonths;
        @Setter
        String[] stringsEn, stringsRu;

        public AchievementGroup(double fromAge,
                                @Nullable Double toAge,
                                @ArrayRes int arrayId,
                                double fromMonths,
                                @Nullable Double toMonths) {
            this.fromAge = fromAge;
            this.toAge = toAge;
            this.arrayId = arrayId;
            this.fromMonths = fromMonths;
            this.toMonths = toMonths;
        }

        public int getCount() {
            return Math.min(stringsEn.length, stringsRu.length);
        }

        public String[] getPair(int index) {
            return new String[]{stringsEn[index], stringsRu[index]};
        }
    }
}
