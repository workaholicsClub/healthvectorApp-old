package ru.android.childdiary.domain.interactors.exercises;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.core.ContentObject;
import ru.android.childdiary.domain.core.RepeatParametersContainer;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@Builder(toBuilder = true)
public class ConcreteExercise implements Serializable, RepeatParametersContainer, ContentObject<ConcreteExercise> {
    private static final ConcreteExercise NULL = ConcreteExercise.builder().build();

    Long id;

    Child child;

    Exercise exercise;

    RepeatParameters repeatParameters;

    String name;

    Integer durationInMinutes;

    DateTime dateTime;

    DateTime finishDateTime;

    Boolean isExported;

    Integer notifyTimeInMinutes;

    String note;

    String imageFileName;

    Boolean isDeleted;

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull ConcreteExercise other) {
        return ObjectUtils.contentEquals(getExercise(), other.getExercise())
                && ObjectUtils.contentEquals(getRepeatParameters(), other.getRepeatParameters())
                && ObjectUtils.contentEquals(getName(), other.getName())
                && ObjectUtils.equals(getDurationInMinutes(), other.getDurationInMinutes())
                && ObjectUtils.equalsToMinutes(getDateTime(), other.getDateTime())
                && ObjectUtils.equalsToMinutes(getFinishDateTime(), other.getFinishDateTime())
                && ObjectUtils.isTrue(getIsExported()) == ObjectUtils.isTrue(other.getIsExported())
                && ObjectUtils.equals(getNotifyTimeInMinutes(), other.getNotifyTimeInMinutes())
                && ObjectUtils.contentEquals(getNote(), other.getNote())
                && ObjectUtils.contentEquals(getImageFileName(), other.getImageFileName());
    }
}
