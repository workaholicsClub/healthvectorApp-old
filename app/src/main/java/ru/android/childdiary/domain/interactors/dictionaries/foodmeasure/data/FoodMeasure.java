package ru.android.childdiary.domain.interactors.dictionaries.foodmeasure.data;

import android.support.annotation.NonNull;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.core.data.ContentObject;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@Builder
public class FoodMeasure implements Serializable, ContentObject<FoodMeasure> {
    public static final FoodMeasure NULL = FoodMeasure.builder().build();

    Long id;

    String name;

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull FoodMeasure other) {
        return ObjectUtils.equals(getId(), other.getId())
                && ObjectUtils.contentEquals(getName(), other.getName());
    }
}
