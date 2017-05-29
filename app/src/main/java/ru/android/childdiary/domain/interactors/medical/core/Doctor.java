package ru.android.childdiary.domain.interactors.medical.core;

import android.support.annotation.NonNull;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.core.ContentObject;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@Builder(toBuilder = true)
public class Doctor implements Serializable, ContentObject<Doctor> {
    public static final Doctor NULL = Doctor.builder().build();

    Long id;

    String name;

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull Doctor other) {
        return ObjectUtils.equals(getId(), other.getId())
                && ObjectUtils.contentEquals(getName(), other.getName());
    }
}
