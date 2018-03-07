package ru.android.healthvector.domain.development.testing.data.tests.core;

import java.io.Serializable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.core.LocalizationUtils;

@Value
@Builder
public class Question implements Serializable {
    @NonNull
    String textEn, textRu;

    public String getText() {
        return LocalizationUtils.getLocalizedName(null, textEn, textRu);
    }
}
