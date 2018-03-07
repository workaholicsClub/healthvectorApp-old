package ru.android.healthvector.domain.dictionaries.foodmeasure.data;

import android.support.annotation.NonNull;

import java.io.Serializable;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.android.healthvector.domain.core.data.ContentObject;
import ru.android.healthvector.domain.dictionaries.core.DictionaryItem;
import ru.android.healthvector.utils.ObjectUtils;

@Value
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FoodMeasure extends DictionaryItem implements Serializable, ContentObject<FoodMeasure> {
    public static final FoodMeasure NULL = FoodMeasure.builder().build();

    Long id;

    String nameEn, nameRu, nameUser;

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
