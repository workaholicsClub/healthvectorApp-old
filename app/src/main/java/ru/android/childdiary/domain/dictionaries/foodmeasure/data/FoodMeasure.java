package ru.android.childdiary.domain.dictionaries.foodmeasure.data;

import android.support.annotation.NonNull;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.core.data.ContentObject;
import ru.android.childdiary.domain.dictionaries.core.DictionaryItem;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@Builder
public class FoodMeasure implements Serializable, ContentObject<FoodMeasure>, DictionaryItem {
    public static final FoodMeasure NULL = FoodMeasure.builder().build();

    Long id;

    String nameEn, nameRu, nameUser;

    public String getName() {
        return DictionaryItem.getLocalizedName(this);
    }

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull FoodMeasure other) {
        return ObjectUtils.equals(id, other.id)
                && ObjectUtils.contentEquals(nameEn, other.nameEn)
                && ObjectUtils.contentEquals(nameRu, other.nameRu)
                && ObjectUtils.contentEquals(nameUser, other.nameUser);
    }
}
