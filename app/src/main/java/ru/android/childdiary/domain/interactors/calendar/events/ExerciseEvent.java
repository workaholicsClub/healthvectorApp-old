package ru.android.childdiary.domain.interactors.calendar.events;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.core.ContentObject;
import ru.android.childdiary.domain.interactors.calendar.events.core.LinearGroupFieldType;
import ru.android.childdiary.domain.interactors.calendar.events.core.LinearGroupItem;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.exercises.ConcreteExercise;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExerciseEvent extends MasterEvent implements ContentObject<ExerciseEvent>, LinearGroupItem<ExerciseEvent> {
    private static final ExerciseEvent NULL = ExerciseEvent.builder().build();

    Long id;

    ConcreteExercise concreteExercise;

    String name;

    Integer durationInMinutes;

    String imageFileName;

    @Builder(toBuilder = true)
    private ExerciseEvent(Long masterEventId,
                          EventType eventType,
                          DateTime dateTime,
                          Integer notifyTimeInMinutes,
                          String note,
                          Boolean isDone,
                          Child child,
                          Integer linearGroup,
                          Long id,
                          ConcreteExercise concreteExercise,
                          String name,
                          Integer durationInMinutes,
                          String imageFileName) {
        super(masterEventId, eventType, dateTime, notifyTimeInMinutes, note, isDone, child, linearGroup);
        this.id = id;
        this.concreteExercise = concreteExercise;
        this.name = name;
        this.durationInMinutes = durationInMinutes;
        this.imageFileName = imageFileName;
    }

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull ExerciseEvent other) {
        return contentEquals(this, other)
                && ObjectUtils.contentEquals(getName(), other.getName())
                && ObjectUtils.equals(getDurationInMinutes(), other.getDurationInMinutes())
                && ObjectUtils.contentEquals(getImageFileName(), other.getImageFileName());
    }

    @Override
    public List<LinearGroupFieldType> getChangedFields(@NonNull ExerciseEvent other) {
        List<LinearGroupFieldType> significantFields = new ArrayList<>(
                MasterEvent.getChangedFields(this, other));

        if (!ObjectUtils.contentEquals(getName(), other.getName())) {
            significantFields.add(LinearGroupFieldType.NAME);
        }

        if (!ObjectUtils.equals(getDurationInMinutes(), other.getDurationInMinutes())) {
            significantFields.add(LinearGroupFieldType.DURATION_IN_MINUTES);
        }

        return significantFields;
    }
}
