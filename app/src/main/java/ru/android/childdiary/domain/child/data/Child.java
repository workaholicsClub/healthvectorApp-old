package ru.android.childdiary.domain.child.data;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.core.data.ContentObject;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@Builder(toBuilder = true)
public class Child implements Serializable, ContentObject<Child> {
    public static final Child NULL = Child.builder().build();

    Long id;

    String name;

    LocalDate birthDate;

    // необязательный параметр
    LocalTime birthTime;

    Sex sex;

    // необязательный параметр
    String imageFileName;

    Double birthHeight;

    Double birthWeight;

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull Child other) {
        return ObjectUtils.contentEquals(name, other.name)
                && ObjectUtils.equals(birthDate, other.birthDate)
                && ObjectUtils.equals(birthTime, other.birthTime)
                && sex == other.sex
                && ObjectUtils.contentEquals(imageFileName, other.imageFileName)
                && ObjectUtils.equals(birthHeight, other.birthHeight)
                && ObjectUtils.equals(birthWeight, other.birthWeight);
    }
}
