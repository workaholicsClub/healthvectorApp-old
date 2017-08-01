package ru.android.childdiary.domain.interactors.development.achievement;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.core.ContentObject;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@Builder(toBuilder = true)
public class ConcreteAchievement implements Serializable, ContentObject<ConcreteAchievement> {
    private static final ConcreteAchievement NULL = ConcreteAchievement.builder().build();

    Long id;

    Child child;

    Achievement achievement;

    String name;

    LocalDate date;

    String note;

    String imageFileName;

    Boolean isPredefined;

    Integer orderNumber;

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull ConcreteAchievement other) {
        return ObjectUtils.contentEquals(getAchievement(), other.getAchievement())
                && ObjectUtils.contentEquals(getName(), other.getName())
                && ObjectUtils.equals(getDate(), other.getDate())
                && ObjectUtils.contentEquals(getNote(), other.getNote())
                && ObjectUtils.contentEquals(getImageFileName(), other.getImageFileName());
    }
}
