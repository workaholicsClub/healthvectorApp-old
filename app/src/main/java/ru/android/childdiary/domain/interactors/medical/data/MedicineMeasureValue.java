package ru.android.childdiary.domain.interactors.medical.data;

import android.support.annotation.Nullable;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.dictionaries.medicinemeasure.MedicineMeasure;

@Value
@Builder
public class MedicineMeasureValue implements Serializable {
    @Nullable
    Double amount;

    @Nullable
    MedicineMeasure medicineMeasure;
}
