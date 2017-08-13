package ru.android.childdiary.domain.exercises.data;

import android.support.annotation.NonNull;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.core.data.ContentObject;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@Builder(toBuilder = true)
public class Exercise implements Serializable, ContentObject<Exercise> {
    public static final Exercise NULL = Exercise.builder().build();

    Long id;

    Long serverId;

    String code;

    String name;

    String description;

    boolean exported;

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull Exercise other) {
        return ObjectUtils.equals(getId(), other.getId())
                && ObjectUtils.equals(getServerId(), other.getServerId())
                && ObjectUtils.contentEquals(getCode(), other.getCode())
                && ObjectUtils.contentEquals(getName(), other.getName())
                && ObjectUtils.contentEquals(getDescription(), other.getDescription());
    }
}
