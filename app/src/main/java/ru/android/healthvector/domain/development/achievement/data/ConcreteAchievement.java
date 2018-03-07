package ru.android.healthvector.domain.development.achievement.data;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.healthvector.data.types.AchievementType;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.core.LocalizationUtils;
import ru.android.healthvector.domain.core.data.ContentObject;
import ru.android.healthvector.utils.ObjectUtils;

@Value
@Builder(toBuilder = true)
public class ConcreteAchievement implements Serializable, ContentObject<ConcreteAchievement> {
    private static final ConcreteAchievement NULL = ConcreteAchievement.builder().build();

    Long id;

    Child child;

    AchievementType achievementType;

    String nameEn, nameRu, nameUser;

    LocalDate date;

    String note;

    String imageFileName;

    Boolean isPredefined;

    // fromAge, toAge -- возраст ребенка в месяцах
    Double fromAge, toAge;

    public String getName() {
        return LocalizationUtils.getLocalizedName(nameUser, nameEn, nameRu);
    }

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull ConcreteAchievement other) {
        return getAchievementType() == other.getAchievementType()
                && ObjectUtils.contentEquals(getName(), other.getName())
                && ObjectUtils.equals(getDate(), other.getDate())
                && ObjectUtils.contentEquals(getNote(), other.getNote())
                && ObjectUtils.contentEquals(getImageFileName(), other.getImageFileName());
    }
}
